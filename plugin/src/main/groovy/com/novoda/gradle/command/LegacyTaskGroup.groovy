package com.novoda.gradle.command

import groovy.transform.PackageScope

@Deprecated
@PackageScope
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
