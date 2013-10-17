package com.novoda.gradle.command

import spock.lang.Specification

import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import java.lang.Exception

class AndroidCommandPluginSpec extends Specification {

    def "fails fast if androis plugin is not applied"() {
        given:
        Project project = ProjectBuilder.builder().build()

        when:
        project.apply plugin: AndroidCommandPlugin

        then:
        thrown(Exception)
    }

    def "provides a variant extension"() {
        given:
        Project project = ProjectBuilder.builder().build()

        when:
        project.apply plugin: 'android'
        project.apply plugin: AndroidCommandPlugin

        then:
        project.variant != null
    }
}