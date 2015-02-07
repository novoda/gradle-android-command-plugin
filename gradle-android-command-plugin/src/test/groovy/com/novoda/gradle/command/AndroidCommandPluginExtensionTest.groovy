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
	def projectDir = new File('.')
        def project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        def extension = new AndroidCommandPluginExtension(project)
        extension
    }
}

