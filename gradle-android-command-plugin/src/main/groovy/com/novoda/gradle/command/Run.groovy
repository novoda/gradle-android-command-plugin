package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class Run extends AdbTask {

    @TaskAction
    void exec() {
        def line = ['shell', 'am', 'start', '-a', 'android.intent.action.MAIN', '-c', 'android.intent.category.LAUNCHER', "$packageName/$launchableActivity"]
        assertDeviceAndRunCommand(line)
    }
}
