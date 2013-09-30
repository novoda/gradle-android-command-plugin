package com.novoda.gradle.command

class Apk extends Command {

    def apkPath

    def packageName2 = "${-> packageName()}"
    def launchableActivity = "${-> launchableActivity()}"

    def packageName() {
        String output = "$aapt dump badging $apkPath".execute().text.readLines().find {
            it.startsWith("package:")
        }
        def matcher = output.readLines()[0]=~/name='(.*?)'/
        if (matcher) {
            matcher[0][1]
        }
    }

    def launchableActivity() {
        String output = "$aapt dump badging $apkPath".execute().text.readLines().find {
            it.startsWith("launchable-activity:")
        }
        def matcher = output.readLines()[0]=~/name='(.*?)'/
        if (matcher) {
            matcher[0][1]
        }
    }
}
