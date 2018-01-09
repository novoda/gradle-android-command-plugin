package com.novoda.gradle.command

class InputSpec {

    String name
    Closure script

    InputSpec(name) {
        this.name = name
    }

    void execute(script) {
        this.script = script
    }

}
