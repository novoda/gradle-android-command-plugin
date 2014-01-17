package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class Monkey extends Adb {

    def events

    def getEvents() {
        System.properties['events'] ?: events ?:  50
    }

    @TaskAction
    void exec() {
        runCommand(['shell', 'monkey', '-p', packageName, '-v', getEvents()])
    }
}
