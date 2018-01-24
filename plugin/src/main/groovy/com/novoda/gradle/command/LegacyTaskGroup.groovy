package com.novoda.gradle.command

class LegacyTaskGroup {

  private final AndroidCommandPluginExtension extension

  LegacyTaskGroup(AndroidCommandPluginExtension extension) {
    this.extension = extension
  }

  String groupFor(taskName, variantName) {
    if (extension.sortBySubtasks) {
      "${AndroidCommandPlugin.TASK_GROUP} $taskName"
    } else {
      "${AndroidCommandPlugin.TASK_GROUP} for variant $variantName"
    }
  }
}
