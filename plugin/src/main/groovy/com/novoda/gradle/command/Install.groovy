package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class Install extends AdbTask {

    def customFlags

    @TaskAction
    void exec() {
        def arguments = ['install']
        if (getCustomFlags())
            arguments += getCustomFlags()

        arguments += ['-rd', apkPath]

        assertDeviceAndRunCommand(arguments)
    }

    private getCustomFlags() {
        if (customFlags instanceof Closure)
            customFlags = customFlags.call()
        customFlags ?: ''
    }

    @Override
    protected handleCommandOutput(def text) {
        super.handleCommandOutput(text)
        def matcher = text =~ /Failure \[(.*?)\]/
        if (matcher) {
            throw new GroovyRuntimeException("Installation failed with error: ${matcher[0][1]}")
        }
    }
}
