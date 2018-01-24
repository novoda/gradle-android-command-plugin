package com.novoda.gradle.command

class InstallSpec {

    String name
    String description
    def customFlags

    InstallSpec(name) {
        this.name = name
    }

    void customFlags(Closure customFlags) {
        this.customFlags = customFlags
    }

    void description(String description) {
        this.description = description
    }
}
