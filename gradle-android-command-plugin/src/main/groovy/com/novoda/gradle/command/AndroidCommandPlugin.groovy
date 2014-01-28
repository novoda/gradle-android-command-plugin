package com.novoda.gradle.command

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.ProjectConfigurationException

public class AndroidCommandPlugin implements Plugin<Project> {

    void apply(Project project) {
        if (!project.plugins.hasPlugin('android')) {
            throw new ProjectConfigurationException("The 'android' plugin is required.")
        }
        def extension = project.extensions.create("variant", AndroidCommandPluginExtension, project)
        extension.tasks "run", com.novoda.gradle.command.Run, ["install"]
        extension.tasks "monkey", com.novoda.gradle.command.Monkey, ["install"]
        extension.tasks "clearPrefs", com.novoda.gradle.command.ClearPreferences
    }
}
