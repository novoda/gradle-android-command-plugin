package com.novoda.gradle.command
import org.gradle.api.tasks.Exec

class Command extends Exec {

    def adb = "$System.env.ANDROID_HOME/platform-tools/adb"
    def aapt = "$System.env.ANDROID_HOME/build-tools/$project.android.buildToolsRevision/aapt"
    def deviceId = "${ -> deviceIdProperty() ?: defaultDeviceId()}"

    def deviceIdProperty() {
        System.properties['deviceId']
    }

    def defaultDeviceId() {
        def devices = attachedDevices()
        if (devices.isEmpty()) {
            throw new IllegalStateException("No attached devices found")
        }
        devices[0]
    }

    def attachedDevices() {
        def devices = []
        "$adb devices".execute().text.eachLine { line ->
            def matcher = line=~/^(.*)\tdevice/
            if (matcher) {
                devices << matcher[0][1]
            }
        }
        devices
    }
}
