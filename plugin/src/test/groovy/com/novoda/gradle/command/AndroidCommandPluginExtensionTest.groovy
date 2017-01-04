package com.novoda.gradle.command
import org.gradle.testfixtures.ProjectBuilder

class AndroidCommandPluginExtensionTest extends GroovyTestCase {

    private static final String SDK_PATH = '/some/path'

    void testDefaultAdbPath() {
        def extension = createExtension()
        assert extension.adb != null
        assert extension.adb.contains('adb')
    }

    void testDefaultEvents() {
        def extension = createExtension()
        assert extension.events == AndroidCommandPluginExtension.EVENTS_DEFAULT
    }

    void testDefaultSeed() {
        def extension = createExtension()
        assert extension.seed == null
    }

    void testDefaultCategories() {
        def extension = createExtension()
        assert extension.categories == null
    }

    private static AndroidCommandPluginExtension createExtension() {
        def project = ProjectBuilder.builder()
                .withProjectDir(new File('..'))
                .build()
        def extension = new AndroidCommandPluginExtension(project, SDK_PATH)
        extension
    }
}

