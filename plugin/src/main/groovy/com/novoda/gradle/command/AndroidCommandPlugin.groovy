package com.novoda.gradle.command

import org.gradle.api.DomainObjectCollection
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
        configureDemoMode(project, extension.demoModeContainer)
        configureSystemAnimations(project)

        attachDefaults(project, extension)
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

    private void configureDemoMode(Project project, demoModeContainer) {
        project.tasks.create('enableDemoMode', EnableDemoModeTask) {
            commands = demoModeContainer
        }
        project.tasks.create('disableDemoMode', DisableDemoModeTask)
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

    private void configureSystemAnimations(Project project) {
        project.tasks.create('enableSystemAnimations', SystemAnimations) { enable = true }
        project.tasks.create('disableSystemAnimations', SystemAnimations) { enable = false }
    }

    private static DomainObjectCollection<AdbTask> attachDefaults(Project project, extension) {
        project.tasks.withType(AdbTask) { task ->
            task.conventionMapping.adb = { extension.adb }
            task.conventionMapping.aapt = { extension.aapt }
            task.conventionMapping.deviceId = { extension.deviceId }
        }
    }

}
