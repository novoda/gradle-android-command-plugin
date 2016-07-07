package com.novoda.gradle.command

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.StopExecutionException

public class AndroidCommandPlugin implements Plugin<Project> {

    public final static String TASK_GROUP = "ADB command"

    void apply(Project project) {
        if (!project.plugins.hasPlugin('android')) {
            throw new StopExecutionException("The 'android' plugin is required.")
        }

        def androidExtension = project.android
        String androidHome = getAndroidHome(androidExtension)

        def extension = androidExtension.extensions.create("command", AndroidCommandPluginExtension, project, androidHome)
        extension.task 'installDevice', 'installs the APK on the specified device', Install, ['assemble']
        extension.task 'run', 'installs and runs a APK on the specified device', Run, ['installDevice']
        extension.task 'start', 'runs an already installed APK on the specified device', Run
        extension.task 'stop', 'forcibly stops the app on the specified device', Stop
        extension.task 'monkey', 'calls the monkey command on the specified device', Monkey, ['installDevice']
        extension.task 'enableSystemAnimations', 'enables system animations on the specified device', EnableSystemAnimations
        extension.task 'disableSystemAnimations', 'disables system animations on the specified device', DisableSystemAnimations
        extension.task 'clearPrefs', 'clears the shared preferences on the specified device', ClearPreferences
        extension.task 'uninstallDevice', 'uninstalls the APK from the specific device', Uninstall
    }

    private static String getAndroidHome(androidExtension) {
        def androidHome
        if (androidExtension.hasProperty('sdkHandler')) {
            androidHome = "${androidExtension.sdkHandler.sdkFolder}"
        } else if (androidExtension.hasProperty('sdkDirectory')) {
            androidHome = "${androidExtension.sdkDirectory}"
        } else {
            throw new IllegalStateException('The android plugin is not exposing the SDK folder in an expected way.')
        }
        androidHome
    }
}
