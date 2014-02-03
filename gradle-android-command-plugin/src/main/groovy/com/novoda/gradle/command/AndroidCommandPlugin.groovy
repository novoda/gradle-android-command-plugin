package com.novoda.gradle.command

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.ProjectConfigurationException

public class AndroidCommandPlugin implements Plugin<Project> {

    void apply(Project project) {
        if (!project.plugins.hasPlugin('android')) {
            throw new ProjectConfigurationException("The 'android' plugin is required.")
        }
        def extension = project.android.extensions.create("command", AndroidCommandPluginExtension, project)
        extension.task "run", com.novoda.gradle.command.Run, ["install"]
        extension.task "monkey", com.novoda.gradle.command.Monkey, ["install"]
        extension.task "clearPrefs", com.novoda.gradle.command.ClearPreferences
    }
}
