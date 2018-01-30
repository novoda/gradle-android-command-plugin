package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class Install extends AdbTask {

    InstallExtension installExtension

    /**
     * Manual creation of Install task is deprecated.
     * Please refer to install dsl documentation for details:
     * https://github.com/novoda/gradle-android-command-plugin#install
     */
    @Deprecated
    void setCustomFlags(customFlags) {
        logger.warn """\
                       Manual creation of Install task is deprecated.
                       Please refer to install dsl documentation and modify your task '$name'
                       https://github.com/novoda/gradle-android-command-plugin#install
                       """.stripIndent()
        installExtension = new InstallExtension()
        installExtension.customFlags = customFlags
    }

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
