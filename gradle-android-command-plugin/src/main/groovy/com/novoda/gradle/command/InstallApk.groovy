package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class InstallApk extends Apk {

    private def pluginEx = project.extensions.findByType(AndroidCommandPluginExtension)

    def deviceId

    def getDeviceId() {
        deviceId ?: pluginEx.deviceId
    }

    @TaskAction
    void exec() {
        def line = ["$pluginEx.adb", '-s', getDeviceId(), 'install', '-r', "$apkPath"]
        println line
        println line.execute().text
    }
}
