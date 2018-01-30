package com.novoda.gradle.command

final class RunExtension implements DeviceAware, DescriptionAware {

    String name

    RunExtension(name = null, description = null) {
        this.name = name
        this.description = description
    }
}
