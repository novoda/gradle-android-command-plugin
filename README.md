gradle-android-command-plugin
=============================

Use gradle tasks to run specific command, such as:

- find all devices attached
- select the first one that complies with a custom rule
- install a specific Apk from the available build types + flavors
- clear preferences or do something related to the apk to prepare for tests
- run monkey runner for that specific apk on that specific device


This is particularly useful for CI servers but could be used to speed up IDE development as well

Install
=============================

```groovy
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'com.novoda:gradle-android-command-plugin:1.0'
    }
}
apply plugin: 'android-command'
```

Example
=============================

The plugin makes available new tasks `run<Variant>`, `monkey<Variant>`, `clearPreferences<Variant>`.
Just apply the plugin via

```groovy
apply plugin: 'android-command'
```

If you have a special case for your tasks you can define your own tasks or override
default values as shown below.

```groovy
variant {
    events 1000
}

def hudlDeviceId() {
    def hudlDevices = variant.attachedDevicesWithBrand('hudl')
    if (!hudlDevices) {
        throw new IllegalStateException("No hudl devices found")
    }
    hudlDevices[0]
}

variant.tasks("instHudl", com.novoda.gradle.command.Install) {
    deviceId {hudlDeviceId()}
}
```
