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

```
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

The plugin makes available new tasks run<Variant>, monkey<Variant>, clearPreferences<Variant>.
Just apply the plugin via

```
apply plugin: 'android-command'
```

If you have special needs for your tasks you can define your own tasks or overrride
default values as shown below.

```
def hudlDeviceId() {
    def hudlDevices = variant.attachedDevicesWithBrand('hudl')
    if (!hudlDevices) {
        throw new IllegalStateException("No hudl devices found")
    }
    hudlDevices[0]
}

def readLocalProperties() {
    def properties = new Properties()
    properties.load(project.rootProject.file("local.properties").newDataInputStream())
    properties.getProperty('sdk.dir') ?: variant.androidHome
}

variant {
    androidHome readLocalProperties()
}

variant.tasks "instHudl", com.novoda.gradle.command.Install, {
    deviceId "${-> hudlDeviceId()}"
}

variant.tasks "monkey", com.novoda.gradle.command.Monkey, {
    events 200
}
```
