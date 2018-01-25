package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class Input extends AdbTask {

    InputExtension inputExtension

    /**
     * Manual creation of Input task is deprecated.
     * Please refer to scripting documentation for details:
     * https://github.com/novoda/gradle-android-command-plugin#input-scripting
     */
    @Deprecated
    void setScript(Closure script) {
        logger.warn """\
                       Manual creation of Input task is deprecated.
                       Please refer to scripting documentation to modify your task '$name'
                       https://github.com/novoda/gradle-android-command-plugin#input-scripting
                       """.stripIndent()
        inputExtension = new InputExtension()
        inputExtension.script = script
    }

    void text(String value) {
        input('text', "$value")
    }

    void tap(int x, int y) {
        input('touchscreen', 'tap', x, y)
    }

    void swipe(int startX, int startY, int endX, int endY) {
        input('touchscreen', 'swipe', startX, startY, endX, endY)
    }

    void key(int code) {
        input('keyevent', code)
    }

    void home() {
        key 3
    }

    void back() {
        key 4
    }

    void up() {
        key 19
    }

    void down() {
        key 20
    }

    void left() {
        key 21
    }

    void right() {
        key 22
    }

    void power() {
        key 26
    }

    void clear() {
        key 28
    }

    void tab() {
        key 61
    }

    void enter() {
        key 66
    }

    void unlock() {
        key 82
    }

    private input(... values) {
        runCommand(['shell', 'input', *values])
    }

    @TaskAction
    void exec() {
        assertDeviceConnected()
        inputExtension.script.delegate = this
        inputExtension.script.call()
    }
}
