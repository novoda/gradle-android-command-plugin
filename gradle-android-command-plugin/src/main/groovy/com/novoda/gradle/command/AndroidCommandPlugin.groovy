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
        extension.task 'installDevice', Install, ['assemble']
        extension.task 'run', Run, ['installDevice']
        extension.task 'monkey', Monkey, ['installDevice']
        extension.task 'clearPrefs', ClearPreferences
        extension.task 'uninstallDevice', Uninstall
    }
}
