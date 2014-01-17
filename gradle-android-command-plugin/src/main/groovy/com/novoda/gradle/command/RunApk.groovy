package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class RunApk extends Apk {

    private def pluginEx = project.extensions.findByType(AndroidCommandPluginExtension)

    def deviceId

    def getDeviceId() {
        deviceId ?: pluginEx.deviceId
    }

    @TaskAction
    void exec() {
        def line = ["$pluginEx.adb", '-s', getDeviceId(), 'shell', 'am', 'start', '-a', 'android.intent.action.MAIN', '-c', 'android.intent.category.LAUNCHER', "$packageName/$launchableActivity"]
        println line
        println line.execute().text
    }
}
