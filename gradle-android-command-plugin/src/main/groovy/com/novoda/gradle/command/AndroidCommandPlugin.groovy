package com.novoda.gradle.command

import org.gradle.api.Plugin
import org.gradle.api.Project

public class AndroidCommandPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def hasAppPlugin = project.plugins.hasPlugin com.android.build.gradle.AppPlugin
        def hasLibraryPlugin = project.plugins.hasPlugin com.android.build.gradle.LibraryPlugin
        def log = project.logger

        // Ensure the Android plugin has been added in app or library form, but not both.
        if (!hasAppPlugin && !hasLibraryPlugin) {
            throw new IllegalStateException("The 'android' or 'android-library' plugin is required.")
        } else if (hasAppPlugin && hasLibraryPlugin) {
            throw new IllegalStateException(
                    "Having both 'android' and 'android-library' plugin is not supported.")
        }

        def variants = hasAppPlugin ? project.android.applicationVariants :
                project.android.libraryVariants

        project.android.metaClass.createTasks = { String name, type ->
            String taskType = type.simpleName.capitalize()

            variants.all { variant ->
                String buildType = variant.buildType.name.capitalize()
                def projectFlavorNames = [""]
                if (hasAppPlugin) {
                    // Flavors are only available for the app plugin (e.g., "Free", "Paid").
                    projectFlavorNames = variant.productFlavors.collect { it.name.capitalize() }
                    // TODO support flavor groups... ugh
                    if (projectFlavorNames.isEmpty()) {
                        projectFlavorNames = [""]
                    }
                }
                def projectFlavors = projectFlavorNames.join()

                Apk task = project.tasks.create(name + taskType + buildType + projectFlavors, type)
                task.apkPath = variant.packageApplication.outputFile
            }

            project.tasks.matching {
                it.name.startsWith "$name$taskType"
            }
        }
    }
}
