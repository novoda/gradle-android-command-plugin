package com.novoda.gradle.command

import org.gradle.api.Project

class RunTaskFactory {

    private final Project project
    private final VariantAwareTaskFactory<Run> variantAwareTaskFactory

    RunTaskFactory(Project project) {
        this.project = project
        this.variantAwareTaskFactory = new VariantAwareTaskFactory<>(project)
    }

    def create(variant, RunExtension extension = new RunExtension()) {
        def extensionSuffix = extension.name ? extension.name.capitalize() : ''

        variantAwareTaskFactory.create(
                variant: variant,
                taskName: "run${extensionSuffix}",
                taskType: Run,
                dependsOn: 'installDevice'
        ).configure {
            description = VariantAwareDescription.createFor(variant, extension,
                    defaultDescription: 'installs and runs APK on the specified device')
            group = 'start'
            conventionMapping.deviceId = { extension.deviceId }
        }

        variantAwareTaskFactory.create(
                variant: variant,
                taskName: "start${extensionSuffix}",
                taskType: Run
        ).configure {
            description = VariantAwareDescription.createFor(variant, extension,
                    defaultDescription: 'runs an already installed APK on the specified device')
            group = 'start'
            conventionMapping.deviceId = { extension.deviceId }
        }
    }

}
