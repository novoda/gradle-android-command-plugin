package com.novoda.gradle.command

class InstallExtension {

    String name
    String description
    def customFlags
    def deviceId

    InstallExtension(name, description = null) {
        this.name = name
        this.description = description
    }

    void customFlags(customFlags) {
        this.customFlags = customFlags
    }

    void deviceId(deviceId) {
        this.deviceId = deviceId
    }

    void description(String description) {
        this.description = description
    }
}
