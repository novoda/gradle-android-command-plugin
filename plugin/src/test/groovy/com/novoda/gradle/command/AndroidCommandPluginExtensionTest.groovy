package com.novoda.gradle.command
import org.gradle.testfixtures.ProjectBuilder

class AndroidCommandPluginExtensionTest extends GroovyTestCase {

    private static final String SDK_PATH = '/some/path'

    void testDefaultAdbPath() {
        def extension = createExtension()
        assert extension.adb != null
        assert extension.adb.contains('adb')
    }

    void testDefaultMonkey() {
        def extension = createExtension()
        assert extension.monkey.events == MonkeyExtension.EVENTS_DEFAULT
        assert extension.monkey.seed == null
        assert extension.monkey.categories == []
    }

    private static AndroidCommandPluginExtension createExtension() {
        def project = ProjectBuilder.builder()
                .withProjectDir(new File('..'))
                .build()
        def extension = new AndroidCommandPluginExtension(project, SDK_PATH)
        extension
    }
}

