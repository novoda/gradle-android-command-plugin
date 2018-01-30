package com.novoda.gradle.command

final class InstallExtension extends DeviceAwareExtension {

    String name
    String description
    def customFlags

    InstallExtension(name, description = null) {
        this.name = name
        this.description = description
    }

    void customFlags(customFlags) {
        this.customFlags = customFlags
    }

    void description(String description) {
        this.description = description
    }
}
