package com.novoda.gradle.command
import org.gradle.testfixtures.ProjectBuilder

class AndroidCommandPluginExtensionTest extends GroovyTestCase {

    private static final String SDK_PATH = '/some/path'

    void testDefaultAdbPath() {
        def extension = createExtension()
        assert extension.getAdb() != null
        assert extension.getAdb().contains('adb')
    }

    void testDefaultEvents() {
        def extension = createExtension()
        assert extension.getEvents() == AndroidCommandPluginExtension.EVENTS_DEFAULT
    }

    void testDefaultSeed() {
        def extension = createExtension()
        assert extension.getSeed() == null
    }

    void testDefaultCategories() {
        def extension = createExtension()
        assert extension.getCategories() == null
    }

    private static AndroidCommandPluginExtension createExtension() {
        def project = ProjectBuilder.builder()
                .withProjectDir(new File('..'))
                .build()
        def extension = new AndroidCommandPluginExtension(project, SDK_PATH)
        extension
    }
}

