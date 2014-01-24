package com.novoda.gradle.command
import org.gradle.testfixtures.ProjectBuilder

class AndroidCommandPluginExtensionTest extends GroovyTestCase {

    void testDefaultAdbPath() {
        def extension = createExtension()
        assert extension.getAdb() != null
        assert extension.getAdb().contains('adb')
    }

    void testDefaultAndroidHomePath() {
        def extension = createExtension()
        assert extension.androidHome != null
    }

    private AndroidCommandPluginExtension createExtension() {
        def project = ProjectBuilder.builder().build()
        def extension = new AndroidCommandPluginExtension(project)
        extension
    }
}