package com.novoda.gradle.command

class Apk extends Command {

    def apkPath
    def variationName

    def packageName = "${-> packageName()}"
    def launchableActivity = "${-> launchableActivity()}"

    def packageName() {
        if (apkPath == null) {
            throw new IllegalStateException("No apk found for the task $name")
        }

        String output = "$aapt dump badging $apkPath".execute().text.readLines().find {
            it.startsWith("package:")
        }

        if (output == null) {
            throw new IllegalStateException("Impossible to find package for apk $apkPath")
        }

        def matcher = output.readLines()[0]=~/name='(.*?)'/
        if (matcher) {
            matcher[0][1]
        }
    }

    def launchableActivity() {
        if (apkPath == null) {
            throw new IllegalStateException("No apk found for the task $name")
        }

        String output = "$aapt dump badging $apkPath".execute().text.readLines().find {
            it.startsWith("launchable-activity:")
        }

        if (output == null) {
            throw new IllegalStateException("Impossible to find launchableActivity for apk $apkPath")
        }

        def matcher = output.readLines()[0]=~/name='(.*?)'/
        if (matcher) {
            matcher[0][1]
        }
    }
}
