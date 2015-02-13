package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class Monkey extends AdbTask {

    def events
    def seed

    protected handleCommandOutput(def text) {
        super.handleCommandOutput(text)
        if (text.contains("Monkey aborted"))
            throw new GroovyRuntimeException('Monkey run failed')
    }

    @TaskAction
    void exec() {
        def arguments = ['shell', 'monkey', '-p', packageName, '-v', getEvents()]
        if (getSeed())
            arguments += ['-s', getSeed()]
        assertDeviceAndRunCommand(arguments)
    }

    private getEvents() {
        if (events instanceof Closure)
            events = events.call()
        events ?: pluginEx.events
    }

    private getSeed() {
        if (seed instanceof Closure)
            seed = seed.call()
        seed ?: pluginEx.seed
    }
}
