package com.novoda.gradle.command
import org.gradle.api.Project

class VariantConfigurator {
    private final Project project
    private final String taskName
    private final Class<? extends Adb> taskType

    VariantConfigurator(Project project, String taskName, Class<? extends Adb> taskType) {
        this.taskType = taskType
        this.taskName = taskName
        this.project = project
    }

    def configure(def variant) {
        def buildTypeName = variant.buildType.name.capitalize()
        def projectFlavorNames = variant.productFlavors.collect { it.name.capitalize() }
        def projectFlavorName = projectFlavorNames.join()
        def variationName = projectFlavorName + buildTypeName

        Adb task = project.tasks.create(taskName + variationName, taskType)
        task.apkPath = variant.packageApplication.outputFile
        task.variationName = variationName
    }
}
