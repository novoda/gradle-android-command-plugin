package com.novoda.gradle.command

import org.gradle.api.Action
import org.gradle.api.Project

final class VariantAwareTaskFactory<T extends AdbTask> {

    final Project project

    VariantAwareTaskFactory(Project project) {
        this.project = project
    }

    void create(variant, String taskName, Class<? extends T> taskType, Action<? super T> configure) {
        create(variant, taskName, taskType, null, configure)
    }
    
    void create(variant, String taskName, Class<? extends T> taskType, String dependsOn = null, Action<? super T> configure = {}) {
        def variantName = VariantSuffix.variantNameFor(variant)
        TasksCompat.createTask(project, "$taskName$variantName", taskType) { T task ->
            if (dependsOn) {
                task.dependsOn "$dependsOn$variantName"
            }
            task.apkPath = "${-> variant.outputs[0].outputFile}"
            configure.execute(task)
        }
    }
}
