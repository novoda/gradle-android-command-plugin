package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class Shell extends AdbTask {

    Closure script

    void text(String value) {
        input('text', '"' + value + '"')
    }

    void key(int code) {
        input('keyevent', code)
    }

    void enter() {
        key 66
    }

    void tab() {
        key 61
    }


    void back() {
        key 4
    }

    void down() {
        key 20
    }

    void power() {
        key 26
    }

    void unlock() {
        key 82
    }

    private input(... values) {
        def command = ["shell", "input"]
        command.addAll(values)
        runCommand(command)
    }

    @TaskAction
    void exec() {
        print "exec shell task"
        script.call()
    }
}
