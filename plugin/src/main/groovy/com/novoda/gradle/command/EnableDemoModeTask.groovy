package com.novoda.gradle.command

import groovy.transform.PackageScope
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.tasks.TaskAction

@PackageScope
class EnableDemoModeTask extends AdbTask {

    NamedDomainObjectContainer<DemoModeExtension> commands

    EnableDemoModeTask() {
        this.group = AndroidCommandPlugin.TASK_GROUP
        this.description = "Enables demo mode on the device"
    }

    @TaskAction
    void exec() {
        assertDeviceConnected()
        assertDeviceAtLeastAndroidM()

        allowDemoMode()
        executeDefaults()
        commands.each {
            broadcast it.name, it.extras
        }
    }

    private allowDemoMode() {
        runCommand(['shell', 'settings', 'put', 'global', 'sysui_demo_allowed', '1'])
    }

    private executeDefaults() {
        broadcastDefault 'network', [mobile: 'show', level: '4']
        broadcastDefault 'battery', [level: '100', plugged: 'false']
        broadcastDefault 'network', [wifi: 'show', level: '4']
        broadcastDefault 'clock', [hhmm: '0810']
        broadcastDefault 'notifications', [visible: 'false']
    }

    private broadcastDefault(String name, intentExtras) {
        if (!commands.findByName(name)) {
            broadcast(name, intentExtras)
        }
    }

    private broadcast(String name, Map<String, String> intentExtras = [:]) {
        def args = intentExtras.collectMany { key, value -> ['-e', "$key", "$value"] }
        runCommand([
                'shell', 'am', 'broadcast',
                '-a', 'com.android.systemui.demo',
                '-e', 'command', name,
                *args
        ])
    }

    private assertDeviceAtLeastAndroidM() {
        if (device().sdkVersion() < 23) {
            logger.warn "Connected device must have at least Android Marshmallow (API level 23) to enable Demo Mode"
        }
    }
}
