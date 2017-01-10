package com.novoda.gradle.command

import groovy.transform.Memoized


public class AdbTask extends org.gradle.api.DefaultTask {

    def adb
    def aapt
    def deviceId

    // set automatically by VariantConfigurator
    def apkPath

    // set automatically by VariantConfigurator
    def variationName

    @Memoized
    def getDeviceId() {
        if (deviceId instanceof Closure)
            deviceId = deviceId.call()
        deviceId
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
        AdbCommand command = [adb: getAdb(), deviceId: getDeviceId(), parameters: 'get-state']
        if (command.execute().text != 'device')
            throw new IllegalStateException("No device with ID ${getDeviceId()} found.")
        printDeviceInfo(device)
    }

    private printDeviceInfo(device) {
        println '=========================='
        println device.toString()
        println '=========================='
    }

    protected void runCommand(def parameters) {
        AdbCommand command = [adb: getAdb(), deviceId: getDeviceId(), parameters: parameters]
        logger.info "running command: $command"
        handleCommandOutput(command.execute().text)
    }

    protected handleCommandOutput(def text)  {
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
