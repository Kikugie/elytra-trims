package dev.kikugie.elytratrims.client.config

import dev.kikugie.elytratrims.client.config.RenderMode.valueOf
import dev.kikugie.elytratrims.client.translation
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = LenientEnumSerializer::class)
enum class RenderMode {
    NONE, SELF, OTHERS, ALL;

    val text get() = "elytratrims.config.mode.${name.lowercase()}".translation()
    val tooltip get() = "elytratrims.config.mode.${name.lowercase()}.tooltip".translation()
}

private object LenientEnumSerializer : KSerializer<RenderMode> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("dev.kikugie.elytratrims.config.LenientEnumSerializer", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): RenderMode {
        val name = decoder.decodeString()
        return valueOf(name.uppercase())
    }

    override fun serialize(encoder: Encoder, value: RenderMode) {
        encoder.encodeString(value.name.lowercase())
    }
}