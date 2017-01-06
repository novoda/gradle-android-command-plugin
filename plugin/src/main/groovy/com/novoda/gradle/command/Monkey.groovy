package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class Monkey extends AdbTask {

    protected handleCommandOutput(def text) {
        super.handleCommandOutput(text)
        if (text.toLowerCase().contains("monkey aborted")) {
            throw new GroovyRuntimeException('Monkey run failed')
        }
    }

    @TaskAction
    void exec() {
        Spec monkey = pluginEx.monkey

        logger.info monkey.categories.toString()

        def arguments = ['shell', 'monkey']
        arguments += ['-p', packageName]
        arguments += monkey.categories.collect { ['-c', it] }.flatten()
        arguments += ['-v', monkey.events]
        if (monkey.seed) {
            arguments += ['-s', monkey.seed]
        }
        assertDeviceAndRunCommand(arguments)
    }

    static class Spec {

        private static final int EVENTS_DEFAULT = 10000

        def events
        def seed
        def categories = []

        void events(events) {
            this.events = events
        }

        void seed(seed) {
            this.seed = seed
        }

        void categories(... categories) {
            this.categories.addAll(categories)
        }

        // prefer system property over direct setting to enable commandline arguments
        def getEvents() {
            System.properties['events'] ?: events ?: EVENTS_DEFAULT
        }

        // prefer system property over direct setting to enable commandline arguments
        def getCategories() {
            def systemCategories = System.properties['categories']
            systemCategories ? [systemCategories] : categories
        }

        def getSeed() {
            System.properties['seed'] ?: seed
        }
    }
}
