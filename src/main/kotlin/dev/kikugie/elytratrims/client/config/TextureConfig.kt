package dev.kikugie.elytratrims.client.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class TextureConfig(
    val useBannerTextures: TextureOption,
    val cropTrims: TextureOption,
    val useDarkerTrim: TextureOption,
) {
    constructor(useBannerTextures: Boolean, cropTrims: Boolean, useDarkerTrims: Boolean) : this(
        useBannerTextures.toOption("useBannerTextures"),
        cropTrims.toOption("cropTrims"),
        useDarkerTrims.toOption("useDarkerTrim")
    )

    companion object {
        fun default(): TextureConfig = TextureConfig(
            useBannerTextures = false,
            cropTrims = true,
            useDarkerTrims = false
        )

        val CODEC: Codec<TextureConfig>  = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.BOOL.fieldOf("useBannerTextures").forGetter { it.useBannerTextures.value },
                Codec.BOOL.fieldOf("cropTrims").forGetter { it.cropTrims.value },
                Codec.BOOL.fieldOf("useDarkerTrim").forGetter { it.useDarkerTrim.value }
            ).apply(instance, ::TextureConfig)
        }
    }
}