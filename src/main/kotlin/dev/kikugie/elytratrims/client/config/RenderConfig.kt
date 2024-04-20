package dev.kikugie.elytratrims.client.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.kikugie.elytratrims.client.config.RenderMode.ALL
import dev.kikugie.elytratrims.client.config.RenderType.*
import net.minecraft.util.StringIdentifiable
import java.util.*

class RenderConfig(
    val color: RenderModeOption,
    val patterns: RenderModeOption,
    val trims: RenderModeOption,
    val cape: RenderModeOption,
    val glow: RenderModeOption,
    val global: RenderModeOption,
) {
    constructor(
        color: RenderMode,
        patterns: RenderMode,
        trims: RenderMode,
        cape: RenderMode,
        glow: RenderMode,
        global: RenderMode,
    ) : this(
        color.toOption(),
        patterns.toOption(),
        trims.toOption(),
        cape.toOption(),
        glow.toOption(),
        global.toOption()
    )

    fun effective(type: RenderType): RenderMode {
        val mode = get(type).value
        return if (mode.weight < global.value.weight) mode else global.value
    }

    operator fun get(type: RenderType) = when (type) {
        COLOR -> color
        PATTERNS -> patterns
        TRIMS -> trims
        CAPE -> cape
        GLOW -> glow
        GLOBAL -> global
    }

    operator fun set(type: RenderType, mode: RenderMode) = when (type) {
        COLOR -> color.value = mode
        PATTERNS -> patterns.value = mode
        TRIMS -> trims.value = mode
        CAPE -> cape.value = mode
        GLOW -> glow.value = mode
        GLOBAL -> global.value = mode
    }

    companion object {
        val CODEC: Codec<RenderConfig> = RecordCodecBuilder.create { instance ->
            instance.group(
                RenderMode.CODEC.fieldOf("color").forGetter { it.color.value },
                RenderMode.CODEC.fieldOf("patterns").forGetter { it.patterns.value },
                RenderMode.CODEC.fieldOf("trims").forGetter { it.trims.value },
                RenderMode.CODEC.fieldOf("cape").forGetter { it.cape.value },
                RenderMode.CODEC.fieldOf("glow").forGetter { it.glow.value },
                RenderMode.CODEC.fieldOf("global").forGetter { it.global.value }
            ).apply(instance, ::RenderConfig)
        }
        fun default() = RenderConfig(ALL, ALL, ALL, ALL, ALL, ALL)
    }
}

enum class RenderType : StringIdentifiable {
    COLOR,
    PATTERNS,
    TRIMS,
    CAPE,
    GLOW,
    GLOBAL;

    override fun asString() = name.lowercase()
}

enum class RenderMode(internal val weight: Int) : StringIdentifiable {
    NONE(0),
    SELF(1),
    OTHERS(1),
    ALL(2);

    override fun asString() = name.lowercase()

    companion object {
        val CODEC = StringIdentifiable.createCodec(entries::toTypedArray)
    }
}
