package com.novoda.gradle.command

class DemoModeExtension {

    final name
    final extras = new HashMap<String, String>()

    DemoModeExtension(name) {
        this.name = name
    }

    def methodMissing(String name, args) {
        extras[name] = args[0]
    }

    def propertyMissing(String name, value) {
        extras[name] = value
    }
}
