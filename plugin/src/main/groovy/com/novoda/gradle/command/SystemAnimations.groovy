package com.novoda.gradle.command

import groovy.transform.PackageScope
import org.gradle.api.tasks.TaskAction

@PackageScope
class SystemAnimations extends AdbTask {

    boolean enable

    SystemAnimations() {
        this.group = 'adb device setting'
    }

    void setEnable(boolean enable) {
        this.enable = enable
        this.description = "${enable ? 'Enables' : 'Disables'} system animations on the device"
    }

    @TaskAction
    void exec() {
        assertDeviceConnected()
        windowAnimations()
        transitionAnimations()
        animatorDuration()
    }

    private windowAnimations() {
        def arguments = ['shell', 'settings', 'put', 'global', 'window_animation_scale', enable ? '1' : '0']
        runCommand(arguments)
    }

    private transitionAnimations() {
        def arguments = ['shell', 'settings', 'put', 'global', 'transition_animation_scale', enable ? '1' : '0']
        runCommand(arguments)
    }

    private animatorDuration() {
        def arguments = ['shell', 'settings', 'put', 'global', 'animator_duration_scale', enable ? '1' : '0']
        runCommand(arguments)
    }

}
