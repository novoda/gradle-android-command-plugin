package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class Install extends AdbTask {

    @TaskAction
    void exec() {
        runCommand(['install', '-r', apkPath])
    }
}
