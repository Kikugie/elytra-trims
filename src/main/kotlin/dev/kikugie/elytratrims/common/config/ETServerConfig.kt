package dev.kikugie.elytratrims.common.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.kikugie.elytratrims.platform.ModStatus
import java.nio.file.Path

class ETServerConfig(
    val addTrims: Boolean,
    val addPatterns: Boolean,
    val addGlow: Boolean,
    val cleanableElytra: Boolean,
    val requireClientSide: Boolean
) {
    companion object {
        val CODEC: Codec<ETServerConfig> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.BOOL.fieldOf("addTrims").forGetter { it.addTrims },
                    Codec.BOOL.fieldOf("addPatterns").forGetter { it.addPatterns },
                    Codec.BOOL.fieldOf("addGlow").forGetter { it.addGlow },
                    Codec.BOOL.fieldOf("cleanableElytra").forGetter { it.cleanableElytra },
                    Codec.BOOL.fieldOf("requireClientSide").forGetter { it.requireClientSide }
                ).apply(instance, ::ETServerConfig)
            }
        private val CONFIG_FILE: Path = ModStatus.configDir.resolve("elytra-trims-server.json")
        fun load() = ConfigLoader.load(CONFIG_FILE, CODEC, Companion::create)
        fun create() = ETServerConfig(
            addTrims = true,
            addPatterns = true,
            addGlow = true,
            cleanableElytra = true,
            requireClientSide = false
        )
    }
}