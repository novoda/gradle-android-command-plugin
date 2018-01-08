package com.novoda.gradle.command

class InputSpec {

    String name
    Closure inputScript

    InputSpec(name) {
        this.name = name
    }

    void inputScript(inputScript) {
        this.inputScript = inputScript
    }

}
