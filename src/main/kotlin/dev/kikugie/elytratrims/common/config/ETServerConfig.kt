package dev.kikugie.elytratrims.common.config

import dev.kikugie.elytratrims.platform.ModStatus
import kotlinx.serialization.Serializable

@Serializable
data class ETServerConfig(
    var requireClientSide: Boolean = false
) {
    companion object {
        fun load() = ETConfigLoader.load(ModStatus.configDir.resolve("elytra-trims-server.json"), ::ETServerConfig)
    }
}
