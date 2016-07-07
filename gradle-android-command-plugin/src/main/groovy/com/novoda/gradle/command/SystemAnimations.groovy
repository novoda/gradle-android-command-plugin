package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class SystemAnimations extends AdbTask {

  Boolean enable;

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
