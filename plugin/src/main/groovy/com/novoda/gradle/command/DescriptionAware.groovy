package com.novoda.gradle.command;

trait DescriptionAware {

    String description

    void description(String description) {
        this.description = description
    }
}
