package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class Run extends AdbTask {

    @TaskAction
    void exec() {
        def line = ['shell', 'am', 'start', '-a', 'android.intent.action.MAIN', '-c', 'android.intent.category.LAUNCHER', "$packageName/$launchableActivity"]
        assertDeviceAndRunCommand(line)
    }

    def getLaunchableActivity() {
        def output = readApkProperty('launchable-activity')
        if (output) {
            def matcher = output.readLines()[0] =~ /name='(.*?)'/
            if (matcher) {
                matcher[0][1]
            }
        } else {
            // Fall back to manually parsing aapt's pseudo-XML output to support activity aliases, see
            // https://code.google.com/p/android/issues/detail?id=157150
            logger.info 'no launchable-activity found, falling back to parsing the manifest'

            output = [pluginEx.aapt, 'dump', 'xmltree', apkPath, 'AndroidManifest.xml'].execute().text

            def it = output.readLines().iterator()
            def nextLine = null

            // Look for activity alias definitions.
            while (it.hasNext()) {
                def line = nextLine ?: it.next()
                nextLine = null

                def matcher = line =~ /(\s+)(E: activity-alias)(.*)/
                if (matcher) {
                    def intentation = matcher[0][1] + '  '
                    def name = null, main = false, launcher = false, disabled = false

                    // Parse the indented block for the current activity alias.
                    while (it.hasNext() && (nextLine = it.next()).startsWith(intentation)) {
                        matcher = nextLine =~ /A: android:name.*="([^"]+)"/
                        if (matcher && !name) {
                            name = matcher[0][1]
                        }
                        main = main || nextLine.contains('android.intent.action.MAIN')
                        launcher = launcher || nextLine.contains('android.intent.category.LAUNCHER')

                        // Exclude disabled entries.
                        disabled = disabled || nextLine ==~ /^(\s+)A: android:enabled.*=.*0x0$/
                    }

                    if (name && main && launcher && !disabled) {
                        // Return the first enabled activity-alias launcher.
                        return name
                    }
                }
            }
        }
    }


}
