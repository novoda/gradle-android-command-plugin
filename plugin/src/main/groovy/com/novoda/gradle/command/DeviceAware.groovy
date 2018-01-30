package com.novoda.gradle.command;

trait DeviceAware {

    def deviceId
    
    void deviceId(deviceId) {
        this.deviceId = deviceId
    }
}
