package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class Install extends Adb {

    @TaskAction
    void exec() {
        runCommand(['install', '-r', apkPath])
    }
}
