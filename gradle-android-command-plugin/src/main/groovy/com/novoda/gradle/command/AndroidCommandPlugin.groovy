package com.novoda.gradle.command

import org.gradle.api.Plugin
import org.gradle.api.Project

public class AndroidCommandPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def hasAppPlugin = project.plugins.hasPlugin com.android.build.gradle.AppPlugin

        if (!hasAppPlugin) {
            throw new IllegalStateException("The 'android' plugin is required.")
        }

        project.android.metaClass.createTasks = { String name, Apk type ->
            String taskType = type.simpleName.capitalize()
            String taskNamePrefix = name + taskType

            project.android.applicationVariants.all { variant ->
                def buildTypeName = variant.buildType.name.capitalize()
                def projectFlavorNames = variant.productFlavors.collect { it.name.capitalize() }
                def projectFlavorName = projectFlavorNames.join()
                def variationName = projectFlavorName + buildTypeName

                Apk task = project.tasks.create(taskNamePrefix + variationName, type)
                task.apkPath = variant.packageApplication.outputFile
            }

            project.tasks.matching {
                it.name.startsWith taskNamePrefix
            }
        }
    }
}
