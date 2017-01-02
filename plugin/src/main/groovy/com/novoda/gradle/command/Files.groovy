package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class Files extends AdbTask {

    Closure script

    List<FileInfo> list(String dir) {
        def infos = new ArrayList<FileInfo>()
        ls(dir).eachLine { line -> infos.add(FileInfo.fromListing(line, dir)) }
        return infos
    }

    String isDir(String value) {
        adb('shell', '[ -d ' + value + ' ] && echo "true"')
    }

    String push(String source, String target) {
        adb('push', source, target)
    }

    String pull(String source, String target) {
        adb('pull', source, target)
    }

    String ls(String dir) {
        shell('ls -la', dir)
    }

    private String adb(String... params) {
        AdbCommand adbCommand = [adb: pluginEx.adb, deviceId: getDeviceId(), parameters: Arrays.asList(params) ]
        adbCommand.execute().text
    }

    private String shell(... values) {
        def parameters = ['shell']
        parameters.addAll(values)
        AdbCommand adbCommand = [adb: pluginEx.adb, deviceId: getDeviceId(), parameters: parameters]
        adbCommand.execute().text
    }

    @TaskAction
    void exec() {
        assertDeviceConnected()
        script.call()
    }
}
