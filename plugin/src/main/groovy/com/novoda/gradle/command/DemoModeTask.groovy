package com.novoda.gradle.command

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.tasks.TaskAction

class DemoModeTask extends AdbTask {

    boolean enable
    NamedDomainObjectContainer<DemoModeExtension> commands

    DemoModeTask() {
        this.group = AndroidCommandPlugin.TASK_GROUP
        this.description = "${enable ? 'Enables' : 'Disables'} demo mode on the connected device"
    }

    @TaskAction
    void exec() {
        assertDeviceConnected()
        assertDeviceAndroidM()
        if (enable) {
            allowDemoMode()
            executeDefaults()
            commands.each {
                broadcast it.name, it.extras
            }
        } else {
            broadcast 'exit'
        }
    }

    private void executeDefaults() {
        broadcastDefault 'network', [mobile: 'show', level: '4']
        broadcastDefault 'battery', [level: '100', plugged: 'false']
        broadcastDefault 'network', [wifi: 'show', level: '4']
        broadcastDefault 'clock', [hhmm: '0810']
        broadcastDefault 'notifications', [visible: 'false']
    }

    private void allowDemoMode() {
        runCommand(['shell', 'settings', 'put', 'global', 'sysui_demo_allowed', '1'])
    }

    def broadcastDefault(String name, intentExtras) {
        if (!commands.findByName(name)) {
            broadcast(name, intentExtras)
        }
    }

    def broadcast(String name, Map<String, String> intentExtras = [:]) {
        def args = intentExtras.collectMany { key, value -> ['-e', "$key", "$value"] }
        runCommand([
                'shell', 'am', 'broadcast',
                '-a', 'com.android.systemui.demo',
                '-e', 'command', name,
                *args
        ])
    }

    void assertDeviceAndroidM() {
        if (device().sdkVersion() < 23) {
            logger.warn "Connected device must have at least Android Marshmallow (API level 23) to enable Demo Mode"
        }
    }
}
