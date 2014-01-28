package com.novoda.gradle.command
import org.gradle.api.Project

class VariantConfigurator {
    private final Project project
    private final String taskName
    private final Class<? extends Adb> taskType
    private final def dependencies

    VariantConfigurator(Project project, String taskName, Class<? extends Adb> taskType, def dependencies) {
        this.taskType = taskType
        this.taskName = taskName
        this.project = project
        this.dependencies = dependencies
    }

    def configure(def variant) {
        def buildTypeName = variant.buildType.name.capitalize()
        def projectFlavorNames = variant.productFlavors.collect { it.name.capitalize() }
        def projectFlavorName = projectFlavorNames.join()
        def variationName = projectFlavorName + buildTypeName

        Adb task = project.tasks.create(taskName + variationName, taskType)
        task.apkPath = variant.packageApplication.outputFile
        task.variationName = variationName

        if (dependencies.size() > 0){
            task.dependsOn << formatDependencies(dependencies, variationName)
        }
    }

    private Set<String> formatDependencies(def dependencies, def variationName){
        dependencies.collect{  "$it$variationName" }
    }
}
