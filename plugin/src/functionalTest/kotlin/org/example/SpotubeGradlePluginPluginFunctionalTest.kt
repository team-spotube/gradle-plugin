package org.example

import java.io.File
import kotlin.test.assertTrue
import kotlin.test.Test
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.io.TempDir

class SpotubeGradlePluginPluginFunctionalTest {

    @field:TempDir
    lateinit var projectDir: File

    private val buildFile by lazy { projectDir.resolve("build.gradle.kts") }
    private val settingsFile by lazy { projectDir.resolve("settings.gradle.kts") }

    @Test fun `generatePluginJson task creates plugin json from DSL`() {
        settingsFile.writeText("")
        buildFile.writeText(
            """
            plugins {
                id("dev.krtirtho.spotube.gradle-plugin")
            }

            spotubePlugin {
                name = "Test Plugin"
                version = "1.0.0"
                apiVersion = "1.0.0"
                description = "A test plugin"
                author = "Test Author"
                contact = "test@example.com"
                repository = "https://github.com/test/plugin"
                bugs = "https://github.com/test/plugin/issues"
                license = "MIT"
                capabilities(dev.krtirtho.PluginCapability.PERSISTENT_STORAGE)
                abilities(dev.krtirtho.PluginAbility.METADATA, dev.krtirtho.PluginAbility.AUDIO)
            }
            """.trimIndent()
        )

        val runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("generatePluginJson")
        runner.withProjectDir(projectDir)
        val result = runner.build()

        val generatedFile = projectDir.resolve("build/generated/plugin.json")
        assertTrue(generatedFile.exists(), "plugin.json should be generated")

        val content = generatedFile.readText()
        assertTrue(content.contains("\"name\": \"Test Plugin\""), "Should contain name")
        assertTrue(content.contains("\"version\": \"1.0.0\""), "Should contain version")
        assertTrue(content.contains("\"author\": \"Test Author\""), "Should contain author")
        assertTrue(content.contains("\"contact\": \"test@example.com\""), "Should contain contact")
        assertTrue(content.contains("\"repository\": \"https://github.com/test/plugin\""), "Should contain repository")
        assertTrue(content.contains("\"bugs\": \"https://github.com/test/plugin/issues\""), "Should contain bugs")
        assertTrue(content.contains("\"license\": \"MIT\""), "Should contain license")
        assertTrue(content.contains("persistent_storage"), "Should contain snake_case capability")
        assertTrue(content.contains("metadata"), "Should contain snake_case ability")
        assertTrue(content.contains("audio"), "Should contain audio ability")
    }
}
