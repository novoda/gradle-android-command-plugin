package com.novoda.gradle.command

import org.gradle.api.Project

class InstallTaskFactory {

    private static final String DEFAULT_DESCRIPTION = 'installs the APK on the specified device'

    private final Project project
    private final VariantAwareTaskFactory<Install> variantAwareTaskFactory

    InstallTaskFactory(Project project) {
        this.project = project
        this.variantAwareTaskFactory = new VariantAwareTaskFactory<>(project)
    }

    void create(variant, InstallExtension extension) {
        variantAwareTaskFactory.create(
                variant, "install${extension.name.capitalize()}", Install, 'assemble'
        ) { task ->
            task.description = VariantAwareDescription.descriptionFor(variant, extension, DEFAULT_DESCRIPTION)
            task.group = 'install'
            task.installExtension = extension
        }
    }

}
