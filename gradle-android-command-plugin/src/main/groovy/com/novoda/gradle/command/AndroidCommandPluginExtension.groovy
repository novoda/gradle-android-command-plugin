package com.novoda.gradle.command
import org.gradle.api.Project

public class AndroidCommandPluginExtension {

    private final Project project

    AndroidCommandPluginExtension(Project project) {
        this.project = project
    }

    @SuppressWarnings("GroovyUnusedDeclaration")
    def tasks(String name, Closure configuration) {
        tasks(name).all(configuration)
    }

    def tasks(String name) {
        tasks(name, Apk)
    }

    @SuppressWarnings("GroovyUnusedDeclaration")
    def tasks(String name, Class<? extends Apk> type, Closure configuration) {
        tasks(name, type).all(configuration)
    }

    def tasks(String name, Class<? extends Apk> type) {
        VariantConfigurator variantConfigurator = new VariantConfigurator(project, name, type);

        project.android.applicationVariants.all {
            variantConfigurator.configure(it)
        }

        project.tasks.matching {
            it.name.startsWith variantConfigurator.taskPrefix()
        }
    }
}
