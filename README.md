gradle-android-command-plugin
=============================
[![](http://ci.novoda.com/buildStatus/icon?job=Gradle%20Android%20Command%20Plugin%20(develop))](http://ci.novoda.com/job/Gradle%20Android%20Command%20Plugin%20(develop)/lastBuild/console)

Use gradle tasks to run specific command, such as:

- find all devices attached and get basic info about them
- select the first one that complies with a custom rule
- install a specific APK from the available build types + flavours
- clear preferences or do something related to the APK to prepare for tests
- run monkeyrunner for that specific APK on that specific device
- uninstall the APK


This is particularly useful for CI servers but could be used to speed up IDE development as well.

Usage
=============================



```groovy
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'com.novoda:gradle-android-command-plugin:1.2.1'
    }
}
apply plugin: 'android-command'
```

It's recommended to use the release version, but if you want to have the current development state you can use the snapshot version like this:

```groovy
buildscript {
    repositories {
        mavenCentral()
        maven {
            url "https://oss.sonatype.org/content/repositories/snapshots/"
        }
    }
    dependencies {
        classpath 'com.novoda:gradle-android-command-plugin:1.3.0-SNAPSHOT'
    }
}
apply plugin: 'android-command'
```



Example
=============================

The plugin makes available new tasks `installDevice<Variant>`, `uninstallDevice<Variant>`, `run<Variant>`, `monkey<Variant>`, `clearPreferences<Variant>`.
Just apply the plugin via

```groovy
apply plugin: 'android-command'
```

If you have a special case for your tasks you can define your own tasks or override
default values as shown below.

```groovy
android {
        events 1000

        // run on device with highest SDK version
        task('runNewest', com.novoda.gradle.command.Run, ['installDevice']) {
            deviceId {
                def device = devices().max({ it.sdkVersion() })
                if (!device) {
                    throw new GroovyRuntimeException('No device found!')
                }
                device.id
            }
        }
}
```


Development
=====================

The development branch of this project is `develop`. Make sure you use `develop` if you want to import the project properly and work on this project.

If you have any errors while importing the project such as not finding the envirnoment variables please run Android Studio from the command line.

OSX eg: `2>/dev/null 1>/dev/null /Applications/Android\ Studio.app/Contents/MacOS/studio &`


License
=======

    (c) Copyright 2014 Novoda

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
