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
        def extension = project.android.extensions.create("command", AndroidCommandPluginExtension, project)
        extension.task 'installDevice', 'installs the APK on the specified device', Install, ['assemble']
        extension.task 'run', 'installs and runs a APK on the specified device', Run, ['installDevice']
        extension.task 'monkey', 'calls the monkey command on the specified device', Monkey, ['installDevice']
        extension.task 'clearPrefs', 'clears the shared preferences on the specified device', ClearPreferences
        extension.task 'uninstallDevice', 'uninstalls the APK from the specific device', Uninstall
    }
}
