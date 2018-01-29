package com.novoda.gradle.command

import org.gradle.api.Project

class VariantConfigurator {
    private final AndroidCommandPluginExtension extension
    private final Project project
    private final String taskName
    private final String description
    private final Class<? extends AdbTask> taskType
    private final def dependencies
    private final LegacyTaskGroup taskGroup

    VariantConfigurator(AndroidCommandPluginExtension extension,
                        Project project,
                        String taskName,
                        String description,
                        Class<? extends AdbTask> taskType,
                        def dependencies) {
        this.extension = extension
        this.project = project
        this.taskType = taskType
        this.taskName = taskName
        this.description = description
        this.dependencies = dependencies
        this.taskGroup = new LegacyTaskGroup(extension)
    }

    def configure(def variant) {
        def variantName = VariantSuffix.variantNameFor(variant)
        AdbTask task = project.tasks.create(taskName + variantName, taskType)

        task.group = taskGroup.groupFor(taskName, variantName)
        task.apkPath = "${-> variant.outputs[0].outputFile}"
        if (description) {
            task.description = "$description for variant $variantName"
        }
        dependencies.each {
            task.dependsOn "$it$variantName"
        }
        return task
    }

}
