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
        def matcher = readApkProperty('package').readLines()[0] =~ /name='(.*?)'/
        if (matcher) {
            matcher[0][1]
        }
    }

    def getLaunchableActivity() {
        def matcher = readApkProperty('launchable-activity').readLines()[0] =~ /name='(.*?)'/
        if (matcher) {
            matcher[0][1]
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
        if (output == null) {
            throw new IllegalStateException("Could not read property '$propertyKey' of $apkPath")
        }
        output
    }
}
