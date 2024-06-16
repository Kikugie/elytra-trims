package dev.kikugie.elytratrims.client.config

import dev.kikugie.elytratrims.client.config.RenderMode.ALL
import dev.kikugie.elytratrims.client.config.RenderType.*
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class RenderConfig(
    @Required @JvmField var color: RenderMode = ALL,
    @Required @JvmField var patterns: RenderMode = ALL,
    @Required @JvmField var trims: RenderMode = ALL,
    @Required @JvmField var cape: RenderMode = ALL,
    @Required @JvmField var glow: RenderMode = ALL,
    @Required @JvmField var global: RenderMode = ALL,
) {
    operator fun get(type: RenderType): RenderMode {
        val mode = getDirect(type)
        return if (mode.ordinal <= global.ordinal) mode else global
    }

    operator fun set(type: RenderType, mode: RenderMode) = when (type) {
        COLOR -> color = mode
        PATTERNS -> patterns = mode
        TRIMS -> trims = mode
        CAPE -> cape = mode
        GLOW -> glow = mode
        GLOBAL -> global = mode
    }

    private fun getDirect(type: RenderType) = when (type) {
        COLOR -> color
        PATTERNS -> patterns
        TRIMS -> trims
        CAPE -> cape
        GLOW -> glow
        GLOBAL -> global
    }
}
