package org.example

import dev.krtirtho.SpotubePluginExtension
import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertFailsWith

class SpotubeGradlePluginPluginTest {
    @Test fun `plugin registers extension and generate task`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("dev.krtirtho.spotube.gradle-plugin")

        assertNotNull(project.extensions.findByName("spotubePlugin"))
        assertNotNull(project.tasks.findByName("generatePluginJson"))
    }

    @Test fun `version accepts valid semver`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("dev.krtirtho.spotube.gradle-plugin")
        val extension = project.extensions.getByType(SpotubePluginExtension::class.java)

        extension.version = "1.0.0"
        extension.version = "0.1.0-alpha"
        extension.version = "2.3.4-beta.1"
        extension.version = "1.0.0+build.123"
        extension.version = "1.0.0-rc.1+build.456"
    }

    @Test fun `version rejects invalid semver`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("dev.krtirtho.spotube.gradle-plugin")
        val extension = project.extensions.getByType(SpotubePluginExtension::class.java)

        assertFailsWith<IllegalArgumentException> { extension.version = "v1.0.0" }
        assertFailsWith<IllegalArgumentException> { extension.version = "1.0" }
        assertFailsWith<IllegalArgumentException> { extension.version = "1" }
        assertFailsWith<IllegalArgumentException> { extension.version = "abc" }
    }

    @Test fun `apiVersion rejects invalid semver`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("dev.krtirtho.spotube.gradle-plugin")
        val extension = project.extensions.getByType(SpotubePluginExtension::class.java)

        assertFailsWith<IllegalArgumentException> { extension.apiVersion = "v1.0.0" }
        assertFailsWith<IllegalArgumentException> { extension.apiVersion = "1.0" }
    }
}
