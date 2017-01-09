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
        extension.task 'start', 'runs an already installed APK on the specified device', Run
        extension.task 'stop', 'forcibly stops the app on the specified device', Stop
        extension.task 'clearPrefs', 'clears the shared preferences on the specified device', ClearPreferences
        extension.task 'uninstallDevice', 'uninstalls the APK from the specific device', Uninstall

        project.afterEvaluate {
            def monkeyTasks = extension.task 'monkey', 'calls the monkey command on the specified device', Monkey, ['installDevice']
            monkeyTasks.all { task ->
                task.monkey = extension.monkey
            }
        }

        defaultTask (project, 'enableSystemAnimations', 'enables system animations on the connected device', SystemAnimations) {
            enable = true
        }
        defaultTask (project, 'disableSystemAnimations', 'disables system animations on the connected device', SystemAnimations) {
            enable = false
        }
    }

    static def defaultTask(Project project, String taskName, String description, Class<? extends AdbTask> taskType, Closure configuration) {
        AdbTask task = project.tasks.create(taskName, taskType)
        task.configure configuration
        task.group = TASK_GROUP
        task.description = description
    }

}
