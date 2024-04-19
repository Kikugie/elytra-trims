package dev.kikugie.elytratrims.client.config

import dev.kikugie.elytratrims.client.translation
import net.minecraft.text.Text

interface Option<T> {
    val default: T
    var value: T

    val id: String
    fun name(): Text
    fun tooltip(): Text
}

class TextureOption(override val default: Boolean, override val id: String) : Option<Boolean> {
    override var value = default
    override fun name() = "elytratrims.config.type.$id".translation()
    override fun tooltip() = "elytratrims.config.type.$id.tooltip".translation()
}

class RenderModeOption(override val default: RenderMode) : Option<RenderMode> {
    override var value = default
    override val id = default.asString()
    override fun name() = "elytratrims.config.type.$id".translation()
    override fun tooltip() = "elytratrims.config.type.$id.tooltip".translation()
}

fun RenderMode.toOption() = RenderModeOption(this)
fun Boolean.toOption(id: String) = TextureOption(this, id)