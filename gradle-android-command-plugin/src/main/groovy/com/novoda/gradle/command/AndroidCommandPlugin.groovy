package com.novoda.gradle.command

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.ProjectConfigurationException

public class AndroidCommandPlugin implements Plugin<Project> {

    public final static String TASK_GROUP = "ADB command"

    void apply(Project project) {
        if (!project.plugins.hasPlugin('android')) {
            throw new ProjectConfigurationException("The 'android' plugin is required.")
        }
        def extension = project.android.extensions.create("command", AndroidCommandPluginExtension, project)
        extension.task 'installDevices', 'installs the APK on all connected devices', Install, ['assemble']
        extension.task 'run', 'installs and runs a APK on all connected devices', Run, ['installDevices']
        extension.task 'monkey', 'calls the monkeyrunner on all connected devices', Monkey, ['installDevices']
        extension.task 'clearPrefs', 'clears the shared preferences on all connected devices', ClearPreferences
        extension.task 'uninstallDevices', 'uninstalls the APK from all connected devices', Uninstall
    }
}
