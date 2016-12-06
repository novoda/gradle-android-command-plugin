package com.novoda.gradle.command

class AdbCommand {

    def adb
    def deviceId
    def parameters

    def execute() {
        command().execute()
    }

    private command() {
        [adb, '-s', deviceId] + parameters
    }

    String toString() {
        command().toString()
    }
}
