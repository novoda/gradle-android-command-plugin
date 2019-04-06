package com.novoda.gradle.command

import org.gradle.api.DomainObjectCollection
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.StopExecutionException

class AndroidCommandPlugin implements Plugin<Project> {

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
            command.installContainer.all { InstallExtension extension ->
                factory.create(variant, extension)
            }
            factory.create(variant, new InstallExtension('device'))
        }
    }

    private static configureStartTasks(AndroidCommandPluginExtension command, Project project) {
        def factory = new RunTaskFactory(project)
        project.android.applicationVariants.all { variant ->
            command.startContainer.all { RunExtension extension ->
                factory.create(variant, extension)
            }
            factory.create(variant)
        }
    }

    private static configureOtherTasks(Project project) {
        def factory = new VariantAwareTaskFactory(project)
        project.android.applicationVariants.all { variant ->

            factory.create(variant, 'stop', Stop) { task ->
                task.description = 'forcibly stops the app on the specified device'
                task.group = 'adb app settings'
            }
            factory.create(variant, 'clearPrefs', ClearPreferences) { task ->
                task.description = 'clears the shared preferences on the specified device'
                task.group = 'adb app settings'
            }
            factory.create(variant, 'uninstallDevice', Uninstall) { task ->
                task.description = 'uninstalls the APK from the specific device'
                task.group = 'install'
            }
        }
    }

    private static configureMonkeyTasks(AndroidCommandPluginExtension command, Project project) {
        def factory = new VariantAwareTaskFactory(project)
        project.android.applicationVariants.all { variant ->

            factory.create(variant, 'monkey', Monkey, 'installDevice') { task ->
                task.description = 'calls the monkey command on the specified device'
                task.group = 'verification'
                task.conventionMapping.monkey = { command.monkey }
            }
        }
    }

    private static configureInputScripts(AndroidCommandPluginExtension command, Project project) {
        command.scriptsContainer.all { InputExtension extension ->
            TasksCompat.createTask(project, extension.name, Input) { task ->
                task.group = 'adb input script'
                task.description = "Runs $extension.name script on the specified device"
                task.inputExtension = extension
            }
        }
    }

    private static configureDemoMode(Project project, demoModeContainer) {
        TasksCompat.createTask(project, 'enableDemoMode', EnableDemoModeTask) { task ->
            task.commands = demoModeContainer
        }
        TasksCompat.createTask(project, 'disableDemoMode', DisableDemoModeTask)
    }

    private static configureSystemAnimations(Project project) {
        TasksCompat.createTask(project, 'enableSystemAnimations', SystemAnimations) { task ->
            task.enable = true
        }
        TasksCompat.createTask(project, 'disableSystemAnimations', SystemAnimations) { task ->
            task.enable = false
        }
    }

    private static DomainObjectCollection<AdbTask> attachDefaults(Project project, extension) {
        TasksCompat.configureEach(project.tasks.withType(AdbTask)) { task ->
            task.conventionMapping.adb = { extension.adb }
            task.conventionMapping.aapt = { extension.aapt }
            task.conventionMapping.deviceId = { extension.deviceId }
        }
    }

}
