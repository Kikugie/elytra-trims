package dev.kikugie.elytratrims.client.config

import dev.kikugie.elytratrims.client.ETClient
import dev.kikugie.elytratrims.client.config.RenderMode.ALL
import dev.kikugie.elytratrims.client.config.RenderType.*
import dev.kikugie.elytratrims.common.config.ETConfigLoader
import dev.kikugie.elytratrims.platform.ModStatus
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class ETClientConfig(
    @JvmField val render: RenderConfig = RenderConfig(),
    @JvmField val texture: TextureConfig = TextureConfig(),
    @JvmField val compat: CompatConfig = CompatConfig(),
) {
    companion object {
        val file = ModStatus.configDir.resolve("elytra-trims.json")
        fun load() = ETConfigLoader.load(file, ::ETClientConfig)
        fun save() = ETConfigLoader.save(file, ETClient.config)
    }
}

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

@Serializable
data class TextureConfig(
    @JvmField var cropTrims: Boolean = true,
    @JvmField var useDarkerTrim: Boolean = false,
    @JvmField var useBannerTextures: Boolean = false,
    @JvmField var useElytraModel: Boolean = true
)

@Serializable
data class CompatConfig(
    @JvmField var smysKeepTrims: Boolean = true,
)