package dev.kikugie.elytratrims.client.config

import dev.kikugie.elytratrims.common.config.ETConfigLoader
import dev.kikugie.elytratrims.platform.ModStatus
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class ETClientConfig(
    @Required @JvmField val render: RenderConfig = RenderConfig(),
    @Required @JvmField val texture: TextureConfig = TextureConfig(),
) {
    companion object {
        fun load() = ETConfigLoader.load(ModStatus.configDir.resolve("elytra-trims.json"), ::ETClientConfig)
    }
}