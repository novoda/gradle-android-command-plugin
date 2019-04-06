package com.novoda.gradle.command

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskCollection
import org.gradle.util.GradleVersion

class TasksCompat {

    private static boolean IS_GRADLE_MIN_49 = GradleVersion.current() >= GradleVersion.version("4.9")

    static <T extends Task> Object createTask(
            Project project,
            String name,
            Class<T> type, Action<? super T> configuration = emptyConfiguration()) {
        if (IS_GRADLE_MIN_49) {
            return project.tasks.register(name, type, configuration)
        } else {
            return project.tasks.create(name, type, configuration)
        }
    }

    static <T extends Task> void configureEach(TaskCollection<T> tasks, Action<? super T> configuration) {
        if (IS_GRADLE_MIN_49) {
            tasks.all(configuration)
        } else {
            tasks.all(configuration)
        }
    }

    private static <T extends Task> Action<? super T> emptyConfiguration() {
        new Action<T>() {
            @Override
            void execute(T t) {

            }
        }
    }

}
