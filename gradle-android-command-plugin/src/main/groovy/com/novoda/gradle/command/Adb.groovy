package com.novoda.gradle.command


public class Adb extends org.gradle.api.DefaultTask {

    protected def pluginEx = project.extensions.findByType(AndroidCommandPluginExtension)

    // set automatically by VariantConfigurator
    def apkPath
    // set automatically by VariantConfigurator
    def variationName

    def deviceId

    def getDeviceId() {
        deviceId ?: pluginEx.deviceId
    }

    def packageName = "${-> packageName()}"
    def launchableActivity = "${-> launchableActivity()}"

    protected runCommand(def parameters) {
        def deviceId = getDeviceId()
        def adbCommand = ["$pluginEx.adb", '-s', deviceId] + parameters
        println "running command: " + adbCommand
        assertDeviceConnected(deviceId)
        println adbCommand.execute().text
    }

    void assertDeviceConnected(def deviceId) {
        if (!pluginEx.attachedDevices().contains(deviceId))
            throw new IllegalStateException("Device "+deviceId+" is not found!")
    }

    protected def packageName() {
        def matcher = readApkProperty('package').readLines()[0] =~ /name='(.*?)'/
        if (matcher) {
            matcher[0][1]
        }
    }

    protected def launchableActivity() {
        def matcher = readApkProperty('launchable-activity').readLines()[0] =~ /name='(.*?)'/
        if (matcher) {
            matcher[0][1]
        }
    }

    private String readApkProperty(String propertyKey) {
        if (apkPath == null) {
            throw new IllegalStateException("No apk found for the task $name")
        }
        String output = ["$pluginEx.aapt", "dump", "badging", "$apkPath"].execute().text.readLines().find {
            it.startsWith("$propertyKey:")
        }
        if (output == null) {
            throw new IllegalStateException("Could not read property '$propertyKey' for apk $apkPath")
        }
        output
    }
}
