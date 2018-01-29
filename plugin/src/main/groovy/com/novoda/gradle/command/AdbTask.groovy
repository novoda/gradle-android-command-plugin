package com.novoda.gradle.command

import groovy.transform.Memoized
import org.gradle.api.DefaultTask

class AdbTask extends DefaultTask {

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
        def deviceId = getDeviceId()
        AdbCommand command = [adb: getAdb(), deviceId: deviceId, parameters: 'get-state']
        if (command.execute().text.trim() != 'device')
            throw new IllegalStateException("No device with ID $deviceId found.")
        printDeviceInfo()
    }

    private printDeviceInfo() {
        println '=========================='
        println device().toString()
        println '=========================='
    }

    protected void runCommand(def parameters) {
        AdbCommand command = [adb: getAdb(), deviceId: getDeviceId(), parameters: parameters]

        logger.info "running command: $command"

        Process process = command.execute()
        if (process.waitFor() != 0) {
            // This error check works only on Nougat+ devices because exit code is always 0 before. http://b.android.com/3254
            throw new GroovyRuntimeException("Adb command failed with error:\n${process.err.text}")
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
        String output = [getAapt(), 'dump', 'badging', apkPath].execute().text.readLines().find {
            it.startsWith("$propertyKey:")
        }
        output
    }

}
