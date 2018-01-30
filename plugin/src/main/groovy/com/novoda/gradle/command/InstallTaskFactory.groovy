package com.novoda.gradle.command

import org.gradle.api.Project

class InstallTaskFactory {

    private static final String DEFAULT_DESCRIPTION = 'installs the APK on the specified device'
    
    final Project project
    private final VariantAwareTaskFactory<Install> variantAwareTaskFactory

    InstallTaskFactory(Project project) {
        this.project = project
        this.variantAwareTaskFactory = new VariantAwareTaskFactory<>(project)
    }

    Install create(variant, InstallExtension extension) {
        Install task = variantAwareTaskFactory.create(variant, "install${extension.name.capitalize()}", Install, 'assemble')

        task.group = 'install'
        task.installExtension = extension

        task.description = descriptionFor(variant, extension, DEFAULT_DESCRIPTION)
        if (extension.deviceId) {
            task.conventionMapping.deviceId = { extension.deviceId }
        }
        task
    }

    private static String descriptionFor(variant, InstallExtension extension, String defaultDescription) {
        def variantName = VariantSuffix.variantNameFor(variant)
        def description = extension.description ?: defaultDescription
        return "$description for $variantName"
    }
}
