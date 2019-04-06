package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class Install extends AdbTask {

    InstallExtension installExtension

    @TaskAction
    void exec() {
        def arguments = ['install']
        if (resolveCustomFlags()) {
            arguments += resolveCustomFlags()
        }

        arguments += ['-r', apkPath]

        assertDeviceAndRunCommand(arguments)
    }

    private resolveCustomFlags() {
        def customFlags = installExtension.customFlags
        if (customFlags instanceof Closure) {
            return customFlags.call() ?: ''
        }
        return customFlags ?: ''
    }

    @Override
    protected handleCommandOutput(def text) {
        super.handleCommandOutput(text)
        def matcher = text =~ /Failure \[(.*?)]/
        if (matcher) {
            throw new GroovyRuntimeException("Installation failed with error: ${matcher[0][1]}")
        }
    }
}
