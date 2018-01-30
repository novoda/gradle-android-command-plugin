package com.novoda.gradle.command

final class RunExtension extends DeviceAwareExtension {

    String name
    String description

    RunExtension(name = null, description = null) {
        this.name = name
        this.description = description
    }

    void description(String description) {
        this.description = description
    }
}
