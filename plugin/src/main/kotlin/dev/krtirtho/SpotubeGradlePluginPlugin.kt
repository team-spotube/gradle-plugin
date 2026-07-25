package dev.krtirtho

import org.gradle.api.tasks.bundling.Zip

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.provider.Provider

class SpotubeGradlePluginPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create(
            "spotubePlugin",
            SpotubePluginExtension::class.java,
        )

        val generatedJsonFile = project.layout.buildDirectory.file("generated/plugin.json")

        val generatePluginJsonTask = project.tasks.register("generatePluginJson") {
            group = "distribution"
            description = "Generates plugin.json from the spotubePlugin DSL configuration."

            onlyIf { extension.isConfigured() }

            outputs.file(generatedJsonFile)

            doLast {
                val metadata = extension.toMetadata()
                val outputFile = generatedJsonFile.get().asFile
                outputFile.parentFile.mkdirs()
                outputFile.writeText(metadata.toJson())
            }
        }

        project.plugins.withId("app.cash.zipline") {
            registerPackageTask(
                project,
                "development",
                "compileDevelopmentExecutableKotlinJsZipline",
                extension,
                generatedJsonFile,
                generatePluginJsonTask,
            )
            registerPackageTask(
                project,
                "production",
                "compileProductionExecutableKotlinJsZipline",
                extension,
                generatedJsonFile,
                generatePluginJsonTask,
            )
        }
    }

    fun registerPackageTask(
        project: Project,
        flavor: String,
        compileTaskName: String,
        extension: SpotubePluginExtension,
        generatedJsonFile: Provider<org.gradle.api.file.RegularFile>,
        generatePluginJsonTask: org.gradle.api.tasks.TaskProvider<*>,
    ) {
        val capitalizedFlavor = flavor.replaceFirstChar { it.uppercase() }
        val projectJsonFile = project.layout.projectDirectory.file("plugin.json")
        val logoFile = project.layout.projectDirectory.file("logo.png")

        project.tasks.register("package${capitalizedFlavor}Plugin", Zip::class.java) {
            group = "distribution"
            description =
                "Packages the $flavor Zipline executable and plugin.json into a smplug."

            dependsOn(project.tasks.named(compileTaskName))
            dependsOn(generatePluginJsonTask)

            archiveFileName.set("plugin-$flavor.smplug")
            destinationDirectory.set(project.layout.buildDirectory.dir("distributions"))

            from(project.layout.buildDirectory.dir("zipline/$capitalizedFlavor")) {
                include("**/*")
            }

            from(project.provider {
                if (extension.isConfigured()) {
                    generatedJsonFile.get().asFile
                } else {
                    projectJsonFile.asFile
                }
            })

            from(logoFile)

            inputs.file(project.provider {
                if (extension.isConfigured()) {
                    generatedJsonFile.get().asFile
                } else {
                    projectJsonFile.asFile
                }
            })
            inputs.file(logoFile)
        }
    }
}
