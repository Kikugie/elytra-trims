package dev.kikugie.elytratrims.client.config

import dev.kikugie.elytratrims.client.translation
import kotlinx.serialization.Serializable

@Serializable
enum class RenderMode {
    NONE, SELF, OTHERS, ALL;
    val text get() = "elytratrims.config.mode.${name.lowercase()}".translation()
    val tooltip get() = "elytratrims.config.mode.${name.lowercase()}.tooltip".translation()
}