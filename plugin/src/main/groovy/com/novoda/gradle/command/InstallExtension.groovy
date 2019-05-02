package com.novoda.gradle.command

final class InstallExtension implements DescriptionAware {

    String name
    def customFlags

    InstallExtension(name, description = null) {
        this.name = name
        this.description = description
    }

    void customFlags(customFlags) {
        this.customFlags = customFlags
    }

}
