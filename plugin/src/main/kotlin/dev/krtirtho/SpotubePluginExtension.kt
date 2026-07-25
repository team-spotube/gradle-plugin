package dev.krtirtho

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import javax.inject.Inject

abstract class SpotubePluginExtension @Inject constructor(
    objects: ObjectFactory,
) {
    private val semverRegex = Regex(
        """^(0|[1-9]\d*)\.(0|[1-9]\d*)\.(0|[1-9]\d*)(?:-((?:0|[1-9]\d*|\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\.(?:0|[1-9]\d*|\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\+([0-9a-zA-Z-]+(?:\.[0-9a-zA-Z-]+)*))?$"""
    )

    private val urlRegex = Regex(
        """^https?://[^\s/$.?#].[^\s]*$"""
    )

    private val spdxRegex = Regex(
        """^[A-Za-z0-9][A-Za-z0-9.\-]+(\+)?$"""
    )

    private fun validateSemver(value: String?, fieldName: String) {
        if (value != null && !semverRegex.matches(value)) {
            throw IllegalArgumentException(
                "Invalid semver format for $fieldName: '$value'. Expected format: MAJOR.MINOR.PATCH (e.g., '1.0.0')"
            )
        }
    }

    private fun validateUrl(value: String?, fieldName: String) {
        if (value != null && !urlRegex.matches(value)) {
            throw IllegalArgumentException(
                "Invalid URL format for $fieldName: '$value'. Expected a valid HTTP/HTTPS URL"
            )
        }
    }

    private fun validateSpdx(value: String?, fieldName: String) {
        if (value != null && !spdxRegex.matches(value)) {
            throw IllegalArgumentException(
                "Invalid SPDX license identifier for $fieldName: '$value'. See https://spdx.org/licenses/"
            )
        }
    }

    private val _name: Property<String> = objects.property(String::class.java)
    var name: String?
        get() = _name.orNull
        set(value) { if (value != null) _name.set(value) }

    private val _version: Property<String> = objects.property(String::class.java)
    var version: String?
        get() = _version.orNull
        set(value) {
            validateSemver(value, "version")
            if (value != null) _version.set(value)
        }

    private val _apiVersion: Property<String> = objects.property(String::class.java)
    var apiVersion: String?
        get() = _apiVersion.orNull
        set(value) {
            validateSemver(value, "apiVersion")
            if (value != null) _apiVersion.set(value)
        }

    private val _description: Property<String> = objects.property(String::class.java)
    var description: String?
        get() = _description.orNull
        set(value) { if (value != null) _description.set(value) }

    private val _author: Property<String> = objects.property(String::class.java)
    var author: String?
        get() = _author.orNull
        set(value) { if (value != null) _author.set(value) }

    private val _contact: Property<String> = objects.property(String::class.java)
    var contact: String?
        get() = _contact.orNull
        set(value) { if (value != null) _contact.set(value) }

    private val _repository: Property<String> = objects.property(String::class.java)
    var repository: String?
        get() = _repository.orNull
        set(value) {
            validateUrl(value, "repository")
            if (value != null) _repository.set(value)
        }

    private val _bugs: Property<String> = objects.property(String::class.java)
    var bugs: String?
        get() = _bugs.orNull
        set(value) {
            validateUrl(value, "bugs")
            if (value != null) _bugs.set(value)
        }

    private val _license: Property<String> = objects.property(String::class.java)
    var license: String?
        get() = _license.orNull
        set(value) {
            validateSpdx(value, "license")
            if (value != null) _license.set(value)
        }

    private val _capabilities: ListProperty<PluginCapability> =
        objects.listProperty(PluginCapability::class.java).convention(emptyList())
    var capabilities: List<PluginCapability>
        get() = _capabilities.get()
        set(value) = _capabilities.set(value)

    private val _abilities: ListProperty<PluginAbility> =
        objects.listProperty(PluginAbility::class.java).convention(emptyList())
    var abilities: List<PluginAbility>
        get() = _abilities.get()
        set(value) = _abilities.set(value)

    fun capabilities(vararg caps: PluginCapability) {
        _capabilities.addAll(caps.toList())
    }

    fun abilities(vararg abs: PluginAbility) {
        _abilities.addAll(abs.toList())
    }

    internal fun isConfigured(): Boolean = _name.isPresent

    fun toMetadata(): PluginMetadata =
        PluginMetadata(
            name = _name.get(),
            version = _version.get(),
            apiVersion = _apiVersion.get(),
            description = _description.get(),
            author = _author.get(),
            contact = _contact.get(),
            repository = _repository.get(),
            bugs = _bugs.get(),
            license = _license.get(),
            capabilities = _capabilities.get(),
            abilities = _abilities.get(),
        )
}
