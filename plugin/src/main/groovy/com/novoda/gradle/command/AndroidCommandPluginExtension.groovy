package com.novoda.gradle.command

import groovy.transform.Memoized
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

public class AndroidCommandPluginExtension {

    def adb
    def aapt
    def deviceId
    def sortBySubtasks

    private final Project project
    private final String androidHome
    private final MonkeySpec monkey
    private final NamedDomainObjectContainer<InputExtension> scriptsContainer
    final NamedDomainObjectContainer<DemoModeExtension> demoModeContainer
    private final NamedDomainObjectContainer<InstallExtension> installContainer

    AndroidCommandPluginExtension(Project project) {
        this(project, findAndroidHomeFrom(project.android))
    }

    AndroidCommandPluginExtension(Project project, String androidHome) {
        this.project = project
        this.androidHome = androidHome
        this.monkey = new MonkeySpec()
        this.demoModeContainer = project.container(DemoModeExtension)
        this.scriptsContainer = project.container(InputExtension)
        this.installContainer = project.container(InstallExtension)
    }

    def task(String name, Class<? extends AdbTask> type, Closure configuration) {
        task(name, type).all(configuration)
    }

    def task(String name, Class<? extends AdbTask> type, def dependencies, Closure configuration) {
        task(name, type, dependencies).all(configuration)
    }

    def task(String name, Class<? extends AdbTask> type, def dependencies = []) {
        task(name, "", type, dependencies)
    }

    def task(String name, String description, Class<? extends AdbTask> type, def dependencies = []) {
        VariantConfigurator variantConfigurator = new VariantConfigurator(this, project, name, description, type, dependencies)
        project.android.applicationVariants.all {
            variantConfigurator.configure(it)
        }
        project.tasks.matching {
            it.name.startsWith name
        }
    }

    def getAdb() {
        adb ?: "$androidHome/platform-tools/adb"
    }

    def getAapt() {
        aapt ?: "$androidHome/build-tools/$project.android.buildToolsRevision/aapt"
    }

    // prefer system property over direct setting to enable commandline arguments
    @Memoized
    def getDeviceId() {
        if (System.properties['deviceId']) {
            return System.properties['deviceId']
        }
        if (deviceId instanceof Closure) {
            return deviceId.call()
        }
        deviceId ?: firstDeviceId()
    }

    void monkey(Action<MonkeySpec> action) {
        action.execute(monkey)
    }

    MonkeySpec getMonkey() {
        monkey
    }

    void demoMode(Action<NamedDomainObjectContainer<DemoModeExtension>> action) {
        action.execute(demoModeContainer)
    }

    void scripts(Action<NamedDomainObjectContainer<InputExtension>> script) {
        script.execute(scriptsContainer)
    }

    NamedDomainObjectContainer<InputExtension> getScripts() {
        scriptsContainer
    }

    void install(Action<NamedDomainObjectContainer<InstallExtension>> install) {
        install.execute(installContainer)
    }

    NamedDomainObjectContainer<InstallExtension> getInstall() {
        installContainer
    }

    void attachDefaults(AdbTask task) {
        task.conventionMapping.adb = { getAdb() }
        task.conventionMapping.aapt = { getAapt() }
        task.conventionMapping.deviceId = { getDeviceId() }
    }

    List<Device> devices() {
        deviceIds().collect { deviceId ->
            new Device(getAdb(), deviceId)
        }
    }

    List<String> deviceIds() {
        def deviceIds = []
        [getAdb(), 'devices'].execute().text.eachLine { line ->
            def matcher = line =~ /^(.*)\tdevice/
            if (matcher) {
                deviceIds << matcher[0][1]
            }
        }
        deviceIds
    }

    private def firstDeviceId() {
        def deviceIds = deviceIds()
        if (deviceIds.isEmpty()) {
            throw new IllegalStateException('No attached devices found')
        }
        deviceIds[0]
    }

    private static def findAndroidHomeFrom(androidExtension) {
        if (androidExtension.hasProperty('sdkHandler')) {
            return "${androidExtension.sdkHandler.sdkFolder}"
        }
        if (androidExtension.hasProperty('sdkDirectory')) {
            return "${androidExtension.sdkDirectory}"
        }
        throw new IllegalStateException('The android plugin is not exposing the SDK folder in an expected way.')
    }

}
