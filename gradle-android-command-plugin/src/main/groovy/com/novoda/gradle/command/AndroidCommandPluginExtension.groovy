package com.novoda.gradle.command

import org.gradle.api.Project

public class AndroidCommandPluginExtension {

    def androidHome
    def adb
    def aapt
    def deviceId
    def events
    def sortBySubtasks

    private final Project project

    AndroidCommandPluginExtension(Project project) {
        this.project = project
        androidHome = readSdkDirFromLocalProperties() ?: System.env.ANDROID_HOME
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
    def getEvents() {
        System.properties['events'] ?: events ?: 10000
    }

    // prefer system property over direct setting to enable commandline arguments
    def devices() {
        if (System.properties['deviceId']){
            return [new Device(getAdb(), System.properties['deviceId'])]
        }
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

    private def readSdkDirFromLocalProperties() {
        try {
            Properties properties = new Properties()
            properties.load(project.rootProject.file('local.properties').newDataInputStream())
            properties.getProperty('sdk.dir').trim()
        }
        catch (Exception e) {
            project.getLogger().debug("could not read local.properties", e)
        }
    }
}
