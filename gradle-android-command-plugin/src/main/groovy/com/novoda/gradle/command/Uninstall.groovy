package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class Uninstall extends Adb {

    @TaskAction
    void exec() {
        runCommand(['uninstall', packageName])
    }
}
