package com.novoda.gradle.command

import groovy.transform.Memoized

public class AdbTask extends org.gradle.api.DefaultTask {

    def adb
    def aapt
    def deviceId

    // set automatically by VariantConfigurator
    def apkPath

    @Memoized
    def getDeviceId() {
        if (deviceId instanceof Closure)
            deviceId = deviceId.call()
        deviceId
    }

    Device device() {
        new Device(getAdb(), getDeviceId())
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

    protected assertDeviceAndRunCommand(def parameters) {
        assertDeviceConnected()
        runCommand(parameters)
    }

    protected void assertDeviceConnected() {
        def adb = getAdb() ?: resolveFromExtension('adb')
        def deviceId = getDeviceId() ?: resolveFromExtension('deviceId')
        AdbCommand command = [adb: adb, deviceId: deviceId, parameters: 'get-state']
        if (command.execute().text.trim() != 'device')
            throw new IllegalStateException("No device with ID $deviceId found.")
        printDeviceInfo(new Device(adb, deviceId))
    }

    private printDeviceInfo(device) {
        println '=========================='
        println device.toString()
        println '=========================='
    }

    protected void runCommand(def parameters) {
        def adb = getAdb() ?: resolveFromExtension('adb')
        def deviceId = getDeviceId() ?: resolveFromExtension('deviceId')
        AdbCommand command = [adb: adb, deviceId: deviceId, parameters: parameters]

        logger.info "running command: $command"

        Process process = command.execute()
        if (process.waitFor() != 0) {
            // This error check works only on Nougat+ devices because exit code is always 0 before. http://b.android.com/3254
            throw new GroovyRuntimeException("Adb command failed with error:\n${process.err.text}");
        }
        handleCommandOutput(process.text)
    }

    /**
     * adb does not always return appropriate exit code and does not write to error stream.
     *
     * So specific tasks need to override {@code handleCommandOutput} method and check the text for errors.
     *
     * See {@link Install}
     * See {@link Monkey}
     */
    protected handleCommandOutput(def text) {
        logger.info text
    }

    protected final readApkProperty(String propertyKey) {
        if (!apkPath) {
            throw new IllegalStateException("No APK found for the '$name' task")
        }
        def aapt = getAapt() ?: resolveFromExtension('aapt')
        String output = [aapt, 'dump', 'badging', apkPath].execute().text.readLines().find {
            it.startsWith("$propertyKey:")
        }
        output
    }

    // TODO: Remove this once we have support for nice DSL support for tasks
    @Deprecated
    final resolveFromExtension(property) {
        logger.warn """\
                       $property not specified for the task '$name'.
                       Automatically resolving $property via the plugin.
                       This support will be removed with the next version of the plugin.
                       Please specify the field $property in your task '$name'.
                       """.stripIndent()
        project.android.command."$property"
    }
}
