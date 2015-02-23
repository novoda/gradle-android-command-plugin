# gradle-android-command-plugin [![](http://ci.novoda.com/buildStatus/icon?job=gradle-android-command-plugin)](http://ci.novoda.com/job/gradle-android-command-plugin/lastSuccessfulBuild/console) [![](https://raw.githubusercontent.com/novoda/novoda/master/assets/btn_apache_lisence.png)](LICENSE.txt)

Use gradle tasks to run specific `adb` commands.


## Description

You can use this plugin to do things such as:

  - Find all devices attached and get basic info about them
  - Select the first one that complies with a custom rule
  - Install a specific APK from the available build types + flavours
  - Clear preferences or do something related to the APK to prepare for tests
  - Run monkey for that specific APK on that specific device
  - Uninstall the APK

This is particularly useful for CI servers but could be used to speed up IDE development as well.


## Adding to your project

To start using this library, add these lines to the `build.gradle` of your project:

```groovy
apply plugin: 'com.android.application'
apply plugin: 'android-command'

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.novoda:gradle-android-command-plugin:1.4.0'
    }
}

```


## Simple usage

The plugin creates new tasks that you can use:

  * `installDevice<Variant>` [`com.novoda.gradle.command.InstallDevice`] - installs the app on a specific device.
  * `uninstallDevice<Variant>` [`com.novoda.gradle.command.UninstallDevice`] - uninstalls the app from a specific device.
  * `run<Variant>` [`com.novoda.gradle.command.Run`] - installs and launches the app on a specific device.
  * `monkey<Variant>` [`com.novoda.gradle.command.Monkey`] - installs and runs monkey on a specific device.
  * `clearPreferences<Variant>` [`com.novoda.gradle.command.ClearPreferences`] - clears app preferences on a specific device.
  * `com.novoda.gradle.command.Input` - runs `input` scripts, wrapping `adb shell input`.
  * `com.novoda.gradle.command.Files` - enables basic file copy via `push` and `pull`, wrapping the respecitve adb calls.

## Links

Here are a list of useful links:

 * We always welcome people to contribute new features or bug fixes, [here is how](https://github.com/novoda/novoda/blob/master/CONTRIBUTING.md)
 * For more info on how start working on this project, [see this wiki page](https://github.com/novoda/gradle-android-command-plugin/wiki/Development-&-Contributing)
 * If you have a problem check the [Issues Page](https://github.com/novoda/gradle-android-command-plugin/issues) first to see if we are working on it
 * For further usage or to delve more deeply checkout the [Project Wiki](https://github.com/novoda/gradle-android-command-plugin/wiki)
 * Looking for community help, browse the already asked [Stack Overflow Questions](http://stackoverflow.com/questions/tagged/support-android-command) or use the tag: `support-android-command` when posting a new question
