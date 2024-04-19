package dev.kikugie.elytratrims.client.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class TextureConfig(
    val useBannerTextures: TextureOption,
    val cropTrims: TextureOption,
    val useDarkerTrims: TextureOption,
) {
    constructor(useBannerTextures: Boolean, cropTrims: Boolean, useDarkerTrims: Boolean) : this(
        useBannerTextures.toOption("useBannerTextures"),
        cropTrims.toOption("cropTrims"),
        useDarkerTrims.toOption("useDarkerTrims")
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
                Codec.BOOL.fieldOf("useDarkerTrims").forGetter { it.useDarkerTrims.value }
            ).apply(instance, ::TextureConfig)
        }
    }
}