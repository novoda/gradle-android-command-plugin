package com.novoda.gradle.command

public class AdbTask extends org.gradle.api.DefaultTask {

    protected pluginEx = project.android.extensions.findByType(AndroidCommandPluginExtension)

    // set automatically by VariantConfigurator
    def apkPath

    // set automatically by VariantConfigurator
    def variationName

    def packageName = "${-> packageName()}"
    def launchableActivity = "${-> launchableActivity()}"

    protected handleCommandOutput(def text) {
        logger.info text
    }

    protected assertDevicesAndRunCommand(def parameters) {
        for (Device device : pluginEx.devices()) {
            assertDeviceConnected(device)
            runCommand(device.id, parameters)
        }
    }

    protected void runCommand(def id, def parameters) {
        AdbCommand command = [adb: pluginEx.getAdb(), deviceId: id, parameters: parameters]
        logger.info "running command: $command"
        handleCommandOutput(command.execute().text)
    }

    private void printDeviceInfo(def device) {
        println '=========================='
        println device.toString()
        println '=========================='
    }

    protected void assertDeviceConnected(def device) {
        if (!pluginEx.deviceIds().contains(device.id))
            throw new IllegalStateException("Device $device.id is not found!")
        printDeviceInfo(device)
    }

    protected packageName() {
        def matcher = readApkProperty('package').readLines()[0] =~ /name='(.*?)'/
        if (matcher) {
            matcher[0][1]
        }
    }

    protected launchableActivity() {
        def matcher = readApkProperty('launchable-activity').readLines()[0] =~ /name='(.*?)'/
        if (matcher) {
            matcher[0][1]
        }
    }

    private String readApkProperty(String propertyKey) {
        if (apkPath == null) {
            throw new IllegalStateException("No apk found for the task $name")
        }
        String output = [pluginEx.aapt, 'dump', 'badging', apkPath].execute().text.readLines().find {
            it.startsWith("$propertyKey:")
        }
        if (output == null) {
            throw new IllegalStateException("Could not read property '$propertyKey' of $apkPath")
        }
        output
    }
}
