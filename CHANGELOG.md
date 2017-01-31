# 1.7.1

_2017-01-31_

- Fix a bug where system animation tasks output a non-actionable warning 6 times. [#119](https://github.com/novoda/gradle-android-command-plugin/pull/119)

# 1.7.0

_2017-01-11_

- Fix a bug where installation error marked as a successful build. [#104](https://github.com/novoda/gradle-android-command-plugin/pull/104)
- Fixed a bug that caused InstallTask to fail on Nougat devices. [#110](https://github.com/novoda/gradle-android-command-plugin/pull/110)
- On Nougat devices, errors in `adb shell` commands will now cause the build to fail forwarding the error. [#110](https://github.com/novoda/gradle-android-command-plugin/pull/110)
- Added a sample showing how to install APKs of all variants in one go. [#109](https://github.com/novoda/gradle-android-command-plugin/pull/109)
- Monkey task configuration is adjusted to make more sense. [#106](https://github.com/novoda/gradle-android-command-plugin/pull/106)

**Breaking changes in Monkey configuration**

Monkey task configuration is adjusted to make more sense. The following adjustments are needed if custom monkey configs are used.

This sample configuration 
```groovy
android {
    ...
    command {
        events 1000
        categories = ['android.intent.category.ONLY_ME']
    }
}
```
needs to be changed. There are 2 possible options:
- Wrap the parameters with a `monkey` closure. (Notice also that `=`  after `categories` is not necessary anymore. Moreover, you now provide multiple categories as a comma separated list.)
```groovy
android {
    ...
    command {
        monkey {
            events 1000
            categories 'android.intent.category.ONLY_ME'
        }
    }
}
```
- If you have a simple configuration, you can also prepend the fields with `monkey`.
```groovy
android {
    ...
    command {
        monkey.events 1000
    }
}
```

# 1.6.2
- Fix source/target compatibility to Java 1.6

# 1.6.1
- Task to enable/disable system animations. [#96](https://github.com/novoda/gradle-android-command-plugin/pull/96)

# 1.6.0
- Make the "start" task not depend on "install" [#83 by Sebastian Schuberth](https://github.com/novoda/gradle-android-command-plugin/pull/83)
- Minor formatting / wording improvements [#84 by Sebastian Schuberth](https://github.com/novoda/gradle-android-command-plugin/pull/84) 
- Feature/add task for Activity Stack [#85](https://github.com/novoda/gradle-android-command-plugin/pull/85)
- Added support to custom flags in install task. [#86 by Sergey Chuvashev](https://github.com/novoda/gradle-android-command-plugin/pull/86)
- Add keyevent for "home" key [#87](https://github.com/novoda/gradle-android-command-plugin/pull/87)
- Move activity detection, add memoization [#88](https://github.com/novoda/gradle-android-command-plugin/pull/88)
- Adds feature to filter monkey by intent category [#90](https://github.com/novoda/gradle-android-command-plugin/pull/90)
- Refactor Category Filter feature [#92](https://github.com/novoda/gradle-android-command-plugin/pull/92)

# 1.5.0
- add support for launching via alias [#79 by Sebastian Schuberth](https://github.com/novoda/gradle-android-command-plugin/pull/79))
- add Stop and Start task [#78 by Sebastian Schuberth](https://github.com/novoda/gradle-android-command-plugin/pull/78))

# 1.4.0

- add Files task to support adb push / pull functionality (see sample project for an example)
- derive adb location from android plugin [#70 by Emanuele Zattin](https://github.com/novoda/gradle-android-command-plugin/pull/70))
- better exception on missing android plugin [#68 by Emanuele Zattin](https://github.com/novoda/gradle-android-command-plugin/pull/68))
- allow to specify seed in monkey task [#67 by Emanuele Zattin](https://github.com/novoda/gradle-android-command-plugin/pull/67))
- fix broken script task on GenyMotion [#59 by Eugen Martynov](https://github.com/novoda/gradle-android-command-plugin/pull/59))
- update to Gradle 2.2 (via wrapper)

# 1.3.0

- add an Input task to support basic adb scripting
- add subgrouping of plugin tasks either by variant name or sub task based on a new setting "sortBySubtasks" (defaults to false)
- updated Gradle 2.1 (via wrapper)
- sample app: android-gradle 0.14, making it compatible with Android Studio 0.9+

# 1.2.1

- added fallback if there is no SDK version available from the device properties
- add descriptions to tasks and group them together (instead of "Other Tasks")

# 1.2.0

Complete rewrite of the plugin.

### New commands

Replaced generic ADB command with useful predefined tasks:
- `installDevice<Variant>`
- `uninstallDevice<Variant>`
- `run<Variant>`
- `monkey<Variant>` - the default number of monkeyrunner events is 10000, change via setting _android.events_
- `clearPreferences<Variant>`

See [README](https://github.com/novoda/gradle-android-command-plugin/blob/master/README.md) for examples.
