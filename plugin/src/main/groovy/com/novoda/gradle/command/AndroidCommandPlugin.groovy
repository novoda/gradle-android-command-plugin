package com.novoda.gradle.command

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.StopExecutionException

class AndroidCommandPlugin implements Plugin<Project> {

    public final static String TASK_GROUP = 'ADB command'

    void apply(Project project) {
        if (!project.plugins.hasPlugin('android')) {
            throw new StopExecutionException("The 'android' plugin is required.")
        }

        AndroidCommandPluginExtension extension = project.android.extensions.create('command', AndroidCommandPluginExtension, project)

        configureInstallTasks(extension, project)

        extension.task 'run', 'installs and runs a APK on the specified device', Run, ['installDevice']
        extension.task 'start', 'runs an already installed APK on the specified device', Run
        extension.task 'stop', 'forcibly stops the app on the specified device', Stop
        extension.task 'clearPrefs', 'clears the shared preferences on the specified device', ClearPreferences
        extension.task 'uninstallDevice', 'uninstalls the APK from the specific device', Uninstall

        def monkeyTasks = extension.task 'monkey', 'calls the monkey command on the specified device', Monkey, ['installDevice']
        monkeyTasks.all {
            conventionMapping.monkey = { extension.monkey }
        }

        configureInputScripts(extension, project)

        defaultTask(project, 'enableSystemAnimations', 'enables system animations on the connected device', SystemAnimations) {
            enable = true
        }
        defaultTask(project, 'disableSystemAnimations', 'disables system animations on the connected device', SystemAnimations) {
            enable = false
        }
        configureDemoMode(project, extension.demoModeContainer)

        project.tasks.withType(AdbTask) { task ->
            extension.attachDefaults(task)
        }
    }

    private void configureDemoMode(Project project, demoModeContainer) {
        project.tasks.create('enableDemoMode', EnableDemoModeTask) {
            commands = demoModeContainer
        }
        project.tasks.create('disableDemoMode', DisableDemoModeTask)
    }

    private configureInputScripts(AndroidCommandPluginExtension command, Project project) {
        command.scriptsContainer.all { extension ->
            project.tasks.create(extension.name, Input) {
                group = 'adb script'
                description = "Runs $extension.name script on the specified device"
                inputExtension = extension
            }
        }
    }

    private static configureInstallTasks(AndroidCommandPluginExtension command, Project project) {
        def factory = new InstallTaskFactory(project)
        project.android.applicationVariants.all { variant ->
            command.installContainer.all { extension ->
                factory.create(variant, extension)
            }
            factory.create(variant, new InstallExtension('device', 'installs the APK on the specified device'))
        }
    }
    
    static defaultTask(Project project, String taskName, String description, Class<? extends AdbTask> taskType, Closure configuration) {
        AdbTask task = project.tasks.create(taskName, taskType)
        task.configure configuration
        task.group = TASK_GROUP
        task.description = description
    }

}
