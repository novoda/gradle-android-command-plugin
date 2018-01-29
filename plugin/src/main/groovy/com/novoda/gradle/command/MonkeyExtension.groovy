package com.novoda.gradle.command

final class MonkeyExtension {

    static final int EVENTS_DEFAULT = 10000

    Integer events
    Integer seed
    List<String> categories = []

    void events(events) {
        this.events = events
    }

    void seed(seed) {
        this.seed = seed
    }

    void categories(... categories) {
        this.categories.addAll(categories)
    }

    // prefer system property over direct setting to enable commandline arguments
    def getEvents() {
        System.properties['events'] ?: events ?: EVENTS_DEFAULT
    }

    // prefer system property over direct setting to enable commandline arguments
    def getCategories() {
        def systemCategories = System.properties['categories']
        systemCategories ? [systemCategories] : categories
    }

    def getSeed() {
        System.properties['seed'] ?: seed
    }
}
