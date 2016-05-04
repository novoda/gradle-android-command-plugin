package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class DisableSystemAnimations extends AdbTask {

  @TaskAction
  void exec() {
    assertDeviceConnected()
    disableWindowAnimations()
    disableTransitionAnimations()
    disableAnimatorDuration()
  }

  private disableWindowAnimations() {
    def arguments = ['shell', 'settings', 'put', 'global', 'window_animation_scale', '0']
    runCommand(arguments)
  }

  private disableTransitionAnimations() {
    def arguments = ['shell', 'settings', 'put', 'global', 'transition_animation_scale', '0']
    runCommand(arguments)
  }

  private disableAnimatorDuration() {
    def arguments = ['shell', 'settings', 'put', 'global', 'animator_duration_scale', '0']
    runCommand(arguments)
  }

}
