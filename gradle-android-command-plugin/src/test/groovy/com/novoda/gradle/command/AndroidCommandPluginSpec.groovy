package com.novoda.gradle.command

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

import java.lang.Exception

class AndroidCommandPluginSpec extends Specification {

    def "provides a variant extension"() {
        given:
        Project project = ProjectBuilder.builder().build()

        when:
        project.apply plugin: AndroidCommandPlugin

        then:
        project.variant != null
    }
}