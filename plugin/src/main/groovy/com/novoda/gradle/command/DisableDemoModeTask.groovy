package com.novoda.gradle.command

import groovy.transform.PackageScope
import org.gradle.api.tasks.TaskAction

@PackageScope
class DisableDemoModeTask extends AdbTask {

    DisableDemoModeTask() {
        this.group = 'adb device setting'
        this.description = 'Disables demo mode on the device'
    }

    @TaskAction
    void exec() {
        assertDeviceConnected()
        runCommand([
                'shell', 'am', 'broadcast',
                '-a', 'com.android.systemui.demo',
                '-e', 'command', 'exit'
        ])
    }
}
