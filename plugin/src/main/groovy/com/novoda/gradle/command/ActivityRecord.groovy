package com.novoda.gradle.command

class ActivityRecord {
    String index
    String packageName
    String activityName

    @Override
    String toString() {
        "#$index: $packageName/$activityName"
    }
}
