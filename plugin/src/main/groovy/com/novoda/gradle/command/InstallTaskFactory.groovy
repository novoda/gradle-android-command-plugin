package com.novoda.gradle.command

import org.gradle.api.Project

class InstallTaskFactory {

    final Project project

    InstallTaskFactory(Project project) {
        this.project = project
    }

    void create(variant, InstallExtension extension) {
        def variantName = VariantSuffix.variantNameFor(variant)
        Install task = project.tasks.create(extension.name + variantName, Install)

        task.dependsOn "assemble$variantName"
        task.group = 'install'
        task.installExtension = extension

        task.apkPath = "${-> variant.outputs[0].outputFile}"
        if (extension.description) {
            task.description = "$extension.description for $variantName"
        }
    }
}
