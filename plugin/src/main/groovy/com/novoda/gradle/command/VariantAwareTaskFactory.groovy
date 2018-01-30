package com.novoda.gradle.command

import org.gradle.api.Project

final class VariantAwareTaskFactory<T extends AdbTask> {

    final Project project

    VariantAwareTaskFactory(Project project) {
        this.project = project
    }

    T create(variant, String taskName, Class<T> taskType, String dependsOn = null) {
        def variantName = VariantSuffix.variantNameFor(variant)
        T task = project.tasks.create("$taskName$variantName", taskType)

        if (dependsOn) {
            task.dependsOn "$dependsOn$variantName"
        }
        task.apkPath = "${-> variant.outputs[0].outputFile}"
        task
    }
}
