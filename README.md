gradle-android-command-plugin
=============================

Use gradle tasks to run specific command, such as:

- find all devices attached
- select the first one that complies with a custom rule
- install a specific Apk from the available build types + flavors
- clear preferences or do something related to the apk to prepare for tests
- run monkey runner for that specific apk on that specific device


This is particularly useful for CI servers but could be used to speed up IDE development as well

Example
=============================

```
apply plugin: 'android-command'

class Hudl extends com.novoda.gradle.command.Apk {

    def defaultDeviceId() {
        def hudlDevices = attachedDevices().findResults { deviceId ->
            def brand = "$adb -s $deviceId shell getprop ro.product.brand".execute()
            String brandName = brand.text.trim()
            brandName == "hudl" ? deviceId : null
        }

        if (hudlDevices.isEmpty()) {
            throw new IllegalStateException("No hudl devices found")
        }

        hudlDevices[0]
    }
}

variant.tasks "install", Hudl, {
    dependsOn "${-> 'assemble' + variationName}"
    doFirst { commandLine "$adb -s $deviceId install -r $apkPath".split(" ") }
}

variant.tasks "clearPreferences", Hudl, {
    doFirst { commandLine "$adb -s $deviceId install -r $apkPath".split(" ") }
}

variant.tasks "run", Hudl, {
    doFirst { commandLine "$adb -s $deviceId shell am start -a android.intent.action.MAIN -c android.intent.category.LAUNCHER $packageName/$launchableActivity".split(" ") }
}

variant.tasks "monkey", Hudl, {
    doFirst { commandLine "$adb -s $deviceId shell monkey -p $packageName -v 50".split(" ") }
}
```
