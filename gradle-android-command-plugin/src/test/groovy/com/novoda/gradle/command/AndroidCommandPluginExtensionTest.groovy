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

    void testDefaultEvents() {
        def extension = createExtension()
        assert extension.getEvents() == 10000
    }

    void testDefaultSeed() {
        def extension = createExtension()
        assert extension.getSeed() == null
    }

    private static AndroidCommandPluginExtension createExtension() {
	def projectDir = new File('..')
        def project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        def extension = new AndroidCommandPluginExtension(project)
        extension
    }
}

