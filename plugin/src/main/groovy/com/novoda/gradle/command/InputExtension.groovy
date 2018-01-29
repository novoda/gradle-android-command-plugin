package com.novoda.gradle.command

final class InputExtension {

    String name
    Closure script

    InputExtension(name) {
        this.name = name
    }

    void execute(script) {
        this.script = script
    }

}
