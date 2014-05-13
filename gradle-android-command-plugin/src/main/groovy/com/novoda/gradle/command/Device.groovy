package com.novoda.gradle.command


public class Device {

    final String adb
    final String id

    Device(String adb, String id) {
        this.adb = adb
        this.id = id
    }

    Integer sdkVersion() {
        try {
            deviceProperty('ro.build.version.sdk').toInteger()
        } catch (NumberFormatException nfe) {
            0
        }

    }

    String androidVersion() {
        deviceProperty('ro.build.version.release')
    }

    String brand() {
        deviceProperty('ro.product.brand')
    }

    String model() {
        deviceProperty('ro.product.model')
    }

    String manufacturer() {
        deviceProperty('ro.product.manufacturer')
    }

    String screenDensity() {
        deviceProperty('ro.sf.lcd_density')
    }

    String country() {
        deviceProperty('persist.sys.country')
    }

    String language() {
        deviceProperty('persist.sys.language')
    }

    String timezone() {
        deviceProperty('persist.sys.timezone')
    }

    String deviceProperty(String key) {
        AdbCommand command = [adb: adb, deviceId: id, parameters: ['shell', 'getprop', key]]
        command.execute().text.trim()
    }

    String toString() {
        def builder = new StringBuilder();
        builder.append("Device ID: $id").append("\n")
        builder.append("SDK version: ${sdkVersion()}").append("\n")
        builder.append("Android version: ${androidVersion()}").append("\n")
        builder.append("Brand: ${brand()}").append("\n")
        builder.append("Model: ${model()}").append("\n")
        builder.append("Manufacturer: ${manufacturer()}").append("\n")
        builder.append("Screen density: ${screenDensity()}").append("\n")
        builder.append("Country: ${country()}").append("\n")
        builder.append("Language: ${language()}").append("\n")
        builder.append("Timezone: ${timezone()}")

        builder.toString()
    }

}
