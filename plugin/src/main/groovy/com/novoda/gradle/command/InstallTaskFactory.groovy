package com.novoda.gradle.command

import org.gradle.api.Project

class InstallTaskFactory {

    final Project project

    InstallTaskFactory(Project project) {
        this.project = project
    }

    void create(variant, InstallSpec spec) {
        def variantName = VariantSuffix.variantNameFor(variant)
        Install task = project.tasks.create(spec.name + variantName, Install)

        task.dependsOn "assemble$variantName"
        task.group = 'install'
        task.installSpec = spec

        task.apkPath = "${-> variant.outputs[0].outputFile}"
        if (spec.description) {
            task.description = "$spec.description for $variantName"
        }
    }
}
