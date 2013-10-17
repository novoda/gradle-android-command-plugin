package com.novoda.gradle.command
import org.gradle.api.Project

class VariantConfigurator {
    private final Project project
    private final String taskName
    private final Class<? extends Apk> taskType

    VariantConfigurator(Project project, String taskName, Class<? extends Apk> taskType) {
        this.taskType = taskType
        this.taskName = taskName
        this.project = project
    }

    def taskPrefix() {
        String taskType = taskType.simpleName.capitalize()
        taskName + taskType
    }

    def configure(def variant) {
        def buildTypeName = variant.buildType.name.capitalize()
        def projectFlavorNames = variant.productFlavors.collect { it.name.capitalize() }
        def projectFlavorName = projectFlavorNames.join()
        def variationName = projectFlavorName + buildTypeName

        Apk task = project.tasks.create(taskPrefix() + variationName, taskType)
        task.apkPath = variant.packageApplication.outputFile
        task.variationName = variationName
    }
}
