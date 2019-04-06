package com.novoda.gradle.command

import org.gradle.api.Project

class RunTaskFactory {

    private static final String RUN_DEFAULT_DESCRIPTION = 'installs and runs APK on the specified device'
    private static final String START_DEFAULT_DESCRIPTION = 'runs an already installed APK on the specified device'

    private final Project project
    private final VariantAwareTaskFactory<Run> variantAwareTaskFactory

    RunTaskFactory(Project project) {
        this.project = project
        this.variantAwareTaskFactory = new VariantAwareTaskFactory<>(project)
    }

    void create(variant, RunExtension extension = new RunExtension()) {
        def extensionSuffix = extension.name ? extension.name.capitalize() : ''

        variantAwareTaskFactory.create(
                variant, "run${extensionSuffix}", Run, 'installDevice'
        ) { task ->
            task.description = VariantAwareDescription.descriptionFor(variant, extension, RUN_DEFAULT_DESCRIPTION)
            task.group = 'adb start'
        }

        variantAwareTaskFactory.create(
                variant, "start${extensionSuffix}", Run
        ) { task ->
            task.description = VariantAwareDescription.descriptionFor(variant, extension, START_DEFAULT_DESCRIPTION)
            task.group = 'adb start'
        }
    }

}
