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

        project.extensions.create("variant", AndroidCommandPluginExtension, project)
    }
}
