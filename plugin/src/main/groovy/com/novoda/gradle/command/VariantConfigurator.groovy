package com.novoda.gradle.command

import org.gradle.api.Project

class VariantConfigurator {
    private final AndroidCommandPluginExtension extension
    private final Project project
    private final String taskName
    private final String description
    private final Class<? extends AdbTask> taskType
    private final def dependencies

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
    }

    def configure(def variant) {
        def variantName = variantName(variant)

        AdbTask task = project.tasks.create(taskName + variantName, taskType)

        if (extension.sortBySubtasks) {
            task.group = "${AndroidCommandPlugin.TASK_GROUP} $taskName"
        } else {
            task.group = "${AndroidCommandPlugin.TASK_GROUP} for variant $variantName"
        }

        task.apkPath = "${-> variant.outputs[0].outputFile}"
        if (description) {
            task.description = "$description for variant ${variantName}"
        }

        dependencies.each {
            task.dependsOn "$it$variantName"
        }
        return task
    }

    private static String variantName(variant) {
        def buildTypeName = variant.buildType.name.capitalize()
        def projectFlavorNames = variant.productFlavors.collect { it.name.capitalize() }
        def projectFlavorName = projectFlavorNames.join()
        return projectFlavorName + buildTypeName
    }
}
