package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class MonkeyApk extends Apk {

    private def pluginEx = project.extensions.findByType(AndroidCommandPluginExtension)

    def deviceId

    def getDeviceId() {
        deviceId ?: pluginEx.deviceId
    }

    @TaskAction
    void exec() {
        def line = [pluginEx.getAdb(), '-s', getDeviceId(), 'shell', 'monkey', '-p', "$packageName", '-v', '50']
        println line
        println line.execute().text
    }
}
