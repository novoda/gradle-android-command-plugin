package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class Monkey extends Adb {

    @TaskAction
    void exec() {
        runCommand(['shell', 'monkey', '-p', packageName, '-v', '50'])
    }
}
