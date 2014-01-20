package com.novoda.gradle.command

import org.gradle.api.Plugin
import org.gradle.api.Project

public class AndroidCommandPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def hasAppPlugin = project.plugins.hasPlugin 'android'

        if (!hasAppPlugin) {
            throw new IllegalStateException("The 'android' plugin is required.")
        }
        def extension = project.extensions.create("variant", AndroidCommandPluginExtension, project)
        extension.tasks "run", com.novoda.gradle.command.Run
        extension.tasks "monkey", com.novoda.gradle.command.Monkey
        extension.tasks "clearPrefs", com.novoda.gradle.command.ClearPreferences

    }
}
