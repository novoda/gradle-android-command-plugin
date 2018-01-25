package com.novoda.gradle.command

class InstallExtension {

    String name
    String description
    def customFlags

    InstallExtension(name) {
        this.name = name
    }

    void customFlags(Closure customFlags) {
        this.customFlags = customFlags
    }

    void description(String description) {
        this.description = description
    }
}
