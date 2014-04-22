package com.novoda.gradle.command


public class Device {

    final String adb
    final String id

    Device(String adb, String id) {
        this.adb = adb
        this.id = id
    }

    Integer sdkVersion() {
        deviceProperty('ro.build.version.sdk').toInteger()
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

}
