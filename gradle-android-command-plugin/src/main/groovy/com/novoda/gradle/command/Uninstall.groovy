package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class Uninstall extends AdbTask {

    @TaskAction
    void exec() {
        assertDevicesAndRunCommand(['uninstall', packageName])
    }
}
