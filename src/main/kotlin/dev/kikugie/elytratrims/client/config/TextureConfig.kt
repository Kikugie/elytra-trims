package dev.kikugie.elytratrims.client.config

import dev.kikugie.elytratrims.client.translation
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import kotlin.reflect.KProperty0

@Serializable
data class TextureConfig(
    @Required @JvmField var cropTrims: Boolean = true,
    @Required @JvmField var useDarkerTrim: Boolean = false,
    @Required @JvmField var useBannerTextures: Boolean = false,
    @Required @JvmField var useElytraModel: Boolean = true
) {
    companion object {
        fun text(prop: KProperty0<Any>) = "elytratrims.config.texture.${prop.name}".translation()
        fun tooltip(prop: KProperty0<Any>) = "elytratrims.config.texture.${prop.name}.tooltip".translation()
    }
}