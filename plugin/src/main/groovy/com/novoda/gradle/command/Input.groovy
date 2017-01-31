package com.novoda.gradle.command

import org.gradle.api.Action
import org.gradle.api.tasks.TaskAction

class Input extends AdbTask {

    InputSpec script = new InputSpec()

    @Deprecated
    void script(Action<InputSpec> action) {
        logger.warn """\
                       Manual creation of Input task is deprecated.
                       Please refer to scripting documentation to modify your task '$name'
                       https://github.com/novoda/gradle-android-command-plugin#input-scripting
                       """.stripIndent()
        action.execute(script)
    }

    @TaskAction
    void exec() {
        assertDeviceConnected()
        script.commands.each {
            runCommand(it)
        }
    }
}
