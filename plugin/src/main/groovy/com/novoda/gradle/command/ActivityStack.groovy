package com.novoda.gradle.command

import org.gradle.api.tasks.TaskAction

class ActivityStack extends AdbTask {

    @TaskAction
    void exec() {
        getActivityRecords().each {
            println it.toString()
        }
    }

    def getActivityRecords() {
        def commandLine = ['shell', 'dumpsys', 'activity', '|', 'grep', '-i', 'run']
        AdbCommand command = [adb: pluginEx.getAdb(), deviceId: getDeviceId(), parameters: commandLine]
        logger.info "running command: $command"
        def output = command.execute().text

        def activityRecords = []
        output.eachLine { line ->
            def matcher = line =~ /Run #([0-9]*): ActivityRecord\{\S* \S* ([^\/]*)\/(\S*)/
            if (matcher) {
                activityRecords << new ActivityRecord(index: matcher[0][1], packageName: matcher[0][2], activityName: matcher[0][3])
            }
        }
        activityRecords
    }
}
