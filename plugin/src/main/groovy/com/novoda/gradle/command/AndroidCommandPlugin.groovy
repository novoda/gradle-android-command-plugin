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
        configureStartTasks(extension, project)
        configureOtherTasks(project)
        configureMonkeyTasks(extension, project)
        configureInputScripts(extension, project)
        configureDemoMode(project, extension.demoModeContainer)
        configureSystemAnimations(project)

        attachDefaults(project, extension)
    }

    private static configureInstallTasks(AndroidCommandPluginExtension command, Project project) {
        def factory = new InstallTaskFactory(project)
        project.android.applicationVariants.all { variant ->
            command.installContainer.all { extension ->
                factory.create(variant, extension)
            }
            factory.create(variant, new InstallExtension('device'))
        }
    }

    private static configureStartTasks(AndroidCommandPluginExtension command, Project project) {
        def factory = new RunTaskFactory(project)
        project.android.applicationVariants.all { variant ->
            command.startContainer.all { extension ->
                factory.create(variant, extension)
            }
            factory.create(variant)
        }
    }

    private static configureOtherTasks(Project project) {
        def factory = new VariantAwareTaskFactory(project)
        project.android.applicationVariants.all { variant ->

            factory.create(variant, 'stop', Stop) {
                description = 'forcibly stops the app on the specified device'
                group = 'adb app settings'
            }
            factory.create(variant, 'clearPrefs', ClearPreferences) {
                description = 'clears the shared preferences on the specified device'
                group = 'adb app settings'
            }
            factory.create(variant, 'uninstallDevice', Uninstall) {
                description = 'uninstalls the APK from the specific device'
                group = 'install'
            }
        }
    }

    private static configureMonkeyTasks(AndroidCommandPluginExtension command, Project project) {
        def factory = new VariantAwareTaskFactory(project)
        project.android.applicationVariants.all { variant ->

            factory.create(variant, 'monkey', Monkey, 'installDevice') {
                description = 'calls the monkey command on the specified device'
                group = 'verification'
                conventionMapping.monkey = { command.monkey }
            }
        }
    }

    private static configureInputScripts(AndroidCommandPluginExtension command, Project project) {
        command.scriptsContainer.all { extension ->
            project.tasks.create(extension.name, Input) {
                group = 'adb input script'
                description = "Runs $extension.name script on the specified device"
                inputExtension = extension
            }
        }
    }

    private static configureDemoMode(Project project, demoModeContainer) {
        project.tasks.create('enableDemoMode', EnableDemoModeTask) {
            commands = demoModeContainer
        }
        project.tasks.create('disableDemoMode', DisableDemoModeTask)
    }

    private static configureSystemAnimations(Project project) {
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
