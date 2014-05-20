package com.novoda.gradle.command

import org.gradle.api.Project

class VariantConfigurator {
    private final Project project
    private final String taskName
    private final String description
    private final Class<? extends AdbTask> taskType
    private final def dependencies

    VariantConfigurator(Project project, String taskName, String description, Class<? extends AdbTask> taskType,
                        def dependencies) {

        this.taskType = taskType
        this.taskName = taskName
        this.description = description
        this.project = project
        this.dependencies = dependencies
    }

    def configure(def variant) {
        def buildTypeName = variant.buildType.name.capitalize()
        def projectFlavorNames = variant.productFlavors.collect { it.name.capitalize() }
        def projectFlavorName = projectFlavorNames.join()
        def variationName = projectFlavorName + buildTypeName

        AdbTask task = project.tasks.create(taskName + variationName, taskType)

        if (task.pluginEx && task.pluginEx.sortBySubtasks) {
            task.group = AndroidCommandPlugin.TASK_GROUP + " " + taskName;
        } else {
            task.group = AndroidCommandPlugin.TASK_GROUP + " for variant " + variationName;
        }

        task.apkPath = "${-> variant.packageApplication.outputFile}"
        if (this.description) {
            task.description = this.description + " for variant ${variationName}"
        }
        task.variationName = variationName

        if (dependencies.size() > 0) {
            task.dependsOn << dependenciesForVariation(variationName)
        }
    }

    private Set<String> dependenciesForVariation(def variationName) {
        dependencies.collect { "$it$variationName" }
    }
}
