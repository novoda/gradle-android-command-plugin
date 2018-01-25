gradle-android-command-plugin
=============================
[![](https://ci.novoda.com/buildStatus/icon?job=gradle-android-command-plugin)](https://ci.novoda.com/job/gradle-android-command-plugin/lastSuccessfulBuild/console) [![](https://raw.githubusercontent.com/novoda/novoda/master/assets/btn_apache_lisence.png)](LICENSE.txt) [![Bintray](https://api.bintray.com/packages/novoda/maven/gradle-android-command-plugin/images/download.svg) ](https://bintray.com/novoda/maven/gradle-android-command-plugin/_latestVersion)

Use gradle tasks to run specific `adb` commands.


Description
-----------

You can use this plugin to do things such as:

  - Find all devices attached and get basic info about them
  - Select the first one that complies with a custom rule
  - Install a specific APK from the available build types + flavours
  - Clear preferences or do something related to the APK to prepare for tests
  - Run monkey for that specific APK on that specific device
  - Uninstall the APK

This is particularly useful for CI servers but could be used to speed up development as well.


Adding to your project
----------------------

To start using this library, add these lines to the `build.gradle` of your project:

```groovy
apply plugin: 'com.android.application'
apply plugin: 'com.novoda.android-command'

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.novoda:gradle-android-command-plugin:1.7.1'
    }
}
```


Simple usage
------------

The plugin creates new tasks that you can use:

  * `installDevice<Variant>` [`com.novoda.gradle.command.InstallDevice`] - installs the app on a specific device.
  * `uninstallDevice<Variant>` [`com.novoda.gradle.command.UninstallDevice`] - uninstalls the app from a specific device.
  * `run<Variant>` [`com.novoda.gradle.command.Run`] - installs and launches the app on a specific device.
  * `start<Variant>` [`com.novoda.gradle.command.Run`] - launches the app on a specific device (without installing it).
  * `stop<Variant>` [`com.novoda.gradle.command.Stop`] - Forcibly stops the app on a specific device.
  * `monkey<Variant>` [`com.novoda.gradle.command.Monkey`] - installs and runs monkey on a specific device.
  * `clearPrefs<Variant>` [`com.novoda.gradle.command.ClearPreferences`] - clears app preferences on a specific device.
  * `com.novoda.gradle.command.Input` - runs `input` scripts, wrapping `adb shell input`.
  * `com.novoda.gradle.command.Files` - enables basic file copy via `push` and `pull`, wrapping the respective adb calls.

For advanced usage please take a look into the sample project [build.gradle](sample/app/build.gradle) file.

Configuration
-------------

### Input Scripting

The plugin has a extension called `scripts` which allows you to do simple scripting automation in a connected device.
Here is an example called `autoLogin` which will input the test username and password into the sample app.

```groovy
scripts {
    autoLogin {
        execute {
          2.times {
              text 'bob'
              enter()
          }
          enter()
        }
    }
}
```

This config will create a gradle task called `autoLogin`. Running `./gradlew autoLogin` will try to input `bob` then press `enter`. This will be done 2 times and then another `enter` will be pressed.

You may have a custom groovy closure to do scripting as you like. The following input methods are available:

```
text(String value)
tap(int x, int y)
swipe(int startX, int startY, int endX, int endY)
key(int code)
home()
back()
up()
down()
left()
right()
clear()
tab()
enter()
power()
unlock()
```


### Install

`installDevice<Variant>` tasks are available by default just to install the app. Plugin also supports `install` dsl to define custom installation tasks.

**customFlags**

Here is an extension called `fromGooglePlay` which will create `installFromGooglePlay<Variant>` tasks.

```groovy
install {
    fromGooglePlay {
        description "Installs with flag Play Store"
        customFlags {
            ['-i', 'com.android.vending']
        }
    }
}
```

**Note:** `customFlags` also supports any custom Closure to be lazy evaluated.

More flags can be found in the `install` section of [the official adb document](https://developer.android.com/studio/command-line/adb.html#pm).

**deviceId**

Here is how you can install on a specific device using `deviceId`

```groovy
install {
    toNewestDevice {
        deviceId {
            def device = devices().max { it.sdkVersion() }
            device.id
        }
    }
}
```

Links
-----

Here are a list of useful links:

 * We always welcome people to contribute new features or bug fixes, [here is how](https://github.com/novoda/novoda/blob/master/CONTRIBUTING.md)
 * For more info on how start working on this project, [see this wiki page](https://github.com/novoda/gradle-android-command-plugin/wiki/Development-&-Contributing)
 * If you have a problem check the [Issues Page](https://github.com/novoda/gradle-android-command-plugin/issues) first to see if we are working on it
 * For further usage or to delve more deeply checkout the [Project Wiki](https://github.com/novoda/gradle-android-command-plugin/wiki)
 * Looking for community help, browse the already asked [Stack Overflow Questions](http://stackoverflow.com/questions/tagged/support-android-command) or use the tag: `support-android-command` when posting a new question
