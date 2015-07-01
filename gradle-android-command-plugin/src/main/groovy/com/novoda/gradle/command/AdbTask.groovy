package com.novoda.gradle.command


public class AdbTask extends org.gradle.api.DefaultTask {

    protected pluginEx = project.android.extensions.findByType(AndroidCommandPluginExtension)

    // set automatically by VariantConfigurator
    def apkPath

    // set automatically by VariantConfigurator
    def variationName

    def deviceId

    def getDeviceId() {
        if (deviceId instanceof Closure)
            deviceId = deviceId.call()
        deviceId ?: pluginEx.deviceId
    }

    def getPackageName() {
        def output = readApkProperty('package')
        if (output) {
            def matcher = output.readLines()[0] =~ /name='(.*?)'/
            if (matcher) {
                matcher[0][1]
            }
        } else {
            throw new IllegalArgumentException("Could not read 'package' property of $apkPath")
        }
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

    protected handleCommandOutput(def text)  {
        logger.info text
    }

    protected assertDeviceAndRunCommand(def parameters) {
        assertDeviceConnected()
        runCommand(parameters)
    }

    protected void runCommand(def parameters) {
        AdbCommand command = [adb: pluginEx.getAdb(), deviceId: getDeviceId(), parameters: parameters]
        logger.info "running command: $command"
        handleCommandOutput(command.execute().text)
    }

    private void printDeviceInfo() {
        Device device = pluginEx.devices().find { device -> device.id == getDeviceId() }
        println '=========================='
        println device.toString()
        println '=========================='
    }

    protected void assertDeviceConnected() {
        def id = getDeviceId()
        if (!pluginEx.deviceIds().contains(id))
            throw new IllegalStateException("Device $id is not found!")
        printDeviceInfo()
    }

    private String readApkProperty(String propertyKey) {
        if (apkPath == null) {
            throw new IllegalStateException("No apk found for the task $name")
        }
        String output = [pluginEx.aapt, 'dump', 'badging', apkPath].execute().text.readLines().find {
            it.startsWith("$propertyKey:")
        }
        output
    }
}
