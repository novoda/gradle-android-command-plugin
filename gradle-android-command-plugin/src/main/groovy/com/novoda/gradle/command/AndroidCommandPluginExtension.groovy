package com.novoda.gradle.command
import org.gradle.api.Project

public class AndroidCommandPluginExtension {

    private final Project project

    AndroidCommandPluginExtension(Project project) {
        this.project = project
    }

    @SuppressWarnings("GroovyUnusedDeclaration")
    def tasks(String name, Closure configuration) {
        tasks(name).all(configuration)
    }

    def tasks(String name) {
        tasks(name, Apk)
    }

    @SuppressWarnings("GroovyUnusedDeclaration")
    def tasks(String name, Class<? extends Apk> type, Closure configuration) {
        tasks(name, type).all(configuration)
    }

    def tasks(String name, Class<? extends Apk> type) {
        String taskType = type.simpleName.capitalize()
        String taskNamePrefix = name + taskType

        project.android.applicationVariants.all { variant ->
            def buildTypeName = variant.buildType.name.capitalize()
            def projectFlavorNames = variant.productFlavors.collect { it.name.capitalize() }
            def projectFlavorName = projectFlavorNames.join()
            def variationName = projectFlavorName + buildTypeName

            Apk task = project.tasks.create(taskNamePrefix + variationName, type)
            task.apkPath = variant.packageApplication.outputFile
            task.variationName = variationName
        }

        project.tasks.matching {
            it.name.startsWith taskNamePrefix
        }
    }
}
