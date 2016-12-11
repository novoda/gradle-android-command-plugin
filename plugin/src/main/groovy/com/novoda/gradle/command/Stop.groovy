package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class Stop extends AdbTask {

    @TaskAction
    void exec() {
        def line = ['shell', 'am', 'force-stop', "$packageName"]
        assertDeviceAndRunCommand(line)
    }
}
