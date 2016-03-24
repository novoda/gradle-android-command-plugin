package com.novoda.gradle.command

import groovy.transform.Memoized
import org.gradle.api.Project

public class AndroidCommandPluginExtension {

    static final int EVENTS_DEFAULT = 10000

    String androidHome
    def adb
    def aapt
    def deviceId
    def events
    def seed
    def categories
    def sortBySubtasks

    private final Project project

    AndroidCommandPluginExtension(Project project, String androidHome) {
        this.project = project
        this.androidHome = androidHome
    }

    def task(String name, Class<? extends AdbTask> type, Closure configuration) {
        task(name, type).all(configuration)
    }

    def task(String name, String description, Class<? extends AdbTask> type) {
        task(name, description, type, [])
    }

    def task(String name, Class<? extends AdbTask> type, def dependencies, Closure configuration) {
        task(name, type, dependencies).all(configuration)
    }

    def task(String name, Class<? extends AdbTask> type) {
        task(name, "", type, [])
    }

    def task(String name, Class<? extends AdbTask> type, def dependencies) {
        task(name, "", type, dependencies)
    }

    def task(String name, String description, Class<? extends AdbTask> type, def dependencies) {
        VariantConfigurator variantConfigurator = new VariantConfigurator(project, name, description, type, dependencies)
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
        deviceId ?: defaultDeviceId()
    }

    // prefer system property over direct setting to enable commandline arguments
    def getEvents() {
        System.properties['events'] ?: events ?: EVENTS_DEFAULT
    }

    // prefer system property over direct setting to enable commandline arguments
    def getCategories() {
        System.properties['categories'] ?: categories
    }

    def getSeed() {
        System.properties['seed'] ?: seed
    }

    def devices() {
        deviceIds().collect { deviceId ->
            new Device(getAdb(), deviceId)
        }
    }

    def deviceIds() {
        def deviceIds = []
        [getAdb(), 'devices'].execute().text.eachLine { line ->
            def matcher = line =~ /^(.*)\tdevice/
            if (matcher) {
                deviceIds << matcher[0][1]
            }
        }
        deviceIds
    }

    private def defaultDeviceId() {
        def deviceIds = deviceIds()
        if (deviceIds.isEmpty()) {
            throw new IllegalStateException('No attached devices found')
        }
        deviceIds[0]
    }
}
