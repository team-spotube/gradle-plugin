package dev.krtirtho

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
enum class PluginCapability {
    @SerialName("persistent_storage")
    PERSISTENT_STORAGE,

    @SerialName("network_requests")
    NETWORK_REQUESTS,

    @SerialName("webview")
    WEBVIEW,
}

@Serializable
enum class PluginAbility {
    @SerialName("metadata")
    METADATA,

    @SerialName("audio")
    AUDIO,

    @SerialName("lyrics")
    LYRICS,

    @SerialName("scrobble")
    SCROBBLE,
}

@Serializable
data class PluginMetadata(
    val name: String,
    val version: String,
    val apiVersion: String,
    val description: String,
    val author: String,
    val capabilities: List<PluginCapability>,
    val abilities: List<PluginAbility>,
) {
    fun toJson(): String = JSON.encodeToString(this)

    companion object {
        val JSON = Json {
            prettyPrint = true
            encodeDefaults = true
        }
    }
}
