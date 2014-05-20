package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class Monkey extends AdbTask {

    private getEvents() {
        pluginEx.events
    }

    protected handleCommandOutput(def text) {
        super.handleCommandOutput(text)
        if (text.contains("Monkey aborted"))
            throw new GroovyRuntimeException('Monkey run failed')
    }

    @TaskAction
    void exec() {
        assertDeviceAndRunCommand(['shell', 'monkey', '-p', packageName, '-v', getEvents()])
    }
}
