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
        MonkeySpec monkey = pluginEx.monkey

        def arguments = ['shell', 'monkey']
        arguments += ['-p', packageName]
        arguments += monkey.categories.collect { ['-c', it] }.flatten()
        arguments += ['-v', monkey.events]
        if (monkey.seed) {
            arguments += ['-s', monkey.seed]
        }
        assertDeviceAndRunCommand(arguments)
    }
}
