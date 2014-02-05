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
        extension.tasks 'installDevice', Install, ['assemble']
        extension.tasks 'run', Run, ['installDevice']
        extension.tasks 'monkey', Monkey, ['installDevice']
        extension.tasks 'clearPrefs', ClearPreferences
    }
}
