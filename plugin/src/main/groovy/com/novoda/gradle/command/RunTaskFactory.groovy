package com.novoda.gradle.command

import org.gradle.api.Project

class RunTaskFactory {

    final Project project
    private final VariantAwareTaskFactory<Run> variantAwareTaskFactory

    RunTaskFactory(Project project) {
        this.project = project
        this.variantAwareTaskFactory = new VariantAwareTaskFactory<>(project)
    }

    def create(variant, RunExtension extension = new RunExtension()) {
        def extensionSuffix = extension.name ? extension.name.capitalize() : ''

        variantAwareTaskFactory.create(variant,
                taskName: "run${extensionSuffix}",
                taskType: Run,
                dependsOn: 'installDevice'
        ).configure {
            group = 'start'
            conventionMapping.deviceId = { extension.deviceId }
            description = descriptionFor(variant, extension, 'installs and runs APK on the specified device')
        }
        
        variantAwareTaskFactory.create(variant,
                taskName: "start${extensionSuffix}",
                taskType: Run
        ).configure {
            group = 'start'
            conventionMapping.deviceId = { extension.deviceId }
            description = descriptionFor(variant, extension, 'runs an already installed APK on the specified device')
        }
    }

    private static String descriptionFor(variant, RunExtension extension, String defaultDescription) {
        def variantName = VariantSuffix.variantNameFor(variant)
        def description = extension.description ?: defaultDescription
        return "$description for $variantName"
    }
}
