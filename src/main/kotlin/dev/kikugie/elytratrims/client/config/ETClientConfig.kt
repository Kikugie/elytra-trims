package dev.kikugie.elytratrims.client.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.kikugie.elytratrims.common.config.ConfigLoader
import dev.kikugie.elytratrims.platform.ModStatus
import java.nio.file.Path

data class ETClientConfig(val render: RenderConfig, val texture: TextureConfig) {
    fun save() = ConfigLoader.save(FILE, CODEC, this)
    companion object {
        val CODEC: Codec<ETClientConfig> = RecordCodecBuilder.create { instance ->
            instance.group(
                RenderConfig.CODEC.fieldOf("render").forGetter { it.render },
                TextureConfig.CODEC.fieldOf("texture").forGetter { it.texture }
            ).apply(instance, ::ETClientConfig)
        }
        val FILE: Path = ModStatus.configDir.resolve("elytra-trims.json")

        fun load(): ETClientConfig = ConfigLoader.load(FILE, CODEC, ::default)
        private fun default() = ETClientConfig(RenderConfig.default(), TextureConfig.default())
    }
}