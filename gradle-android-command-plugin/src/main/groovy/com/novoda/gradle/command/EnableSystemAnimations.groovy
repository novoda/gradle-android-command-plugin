package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class EnableSystemAnimations extends AdbTask {

  @TaskAction
  void exec() {
    assertDeviceConnected()
    enableWindowAnimations()
    enableTransitionAnimations()
    enableAnimatorDuration()
  }

  private enableWindowAnimations() {
    def arguments = ['shell', 'settings', 'put', 'global', 'window_animation_scale', '1']
    runCommand(arguments)
  }

  private enableTransitionAnimations() {
    def arguments = ['shell', 'settings', 'put', 'global', 'transition_animation_scale', '1']
    runCommand(arguments)
  }

  private enableAnimatorDuration() {
    def arguments = ['shell', 'settings', 'put', 'global', 'animator_duration_scale', '1']
    runCommand(arguments)
  }

}
