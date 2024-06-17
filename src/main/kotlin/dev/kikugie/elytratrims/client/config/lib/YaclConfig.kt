package dev.kikugie.elytratrims.client.config.lib

import dev.isxander.yacl3.api.OptionFlag
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import dev.isxander.yacl3.dsl.YetAnotherConfigLib
import dev.isxander.yacl3.dsl.binding
import dev.isxander.yacl3.gui.controllers.cycling.EnumController
import dev.kikugie.elytratrims.client.config.*
import dev.kikugie.elytratrims.client.translation
import dev.kikugie.elytratrims.common.ETReference
import net.minecraft.client.gui.screen.Screen
import kotlin.reflect.KMutableProperty0

fun create(parent: Screen?): Screen = YetAnotherConfigLib(ETReference.MOD_ID) {
    title("elytratrims.config.title".translation())
    val main by categories.registering {
        title("elytratrims.config.category".translation())
        val render by groups.registering {
            val config = RenderConfig()
            title("elytratrims.config.category.render".translation())
            fun create(type: RenderType, prop: KMutableProperty0<RenderMode>) = options.registering {
                name(type.text)
                tooltip(type.tooltip)
                binding(prop, RenderMode.ALL)
                customController { EnumController(it, RenderMode::text, RenderMode.entries.toTypedArray()) }
            }
            create(RenderType.COLOR, config::color)
            create(RenderType.PATTERNS, config::patterns)
            create(RenderType.TRIMS, config::patterns)
            create(RenderType.CAPE, config::cape)
            create(RenderType.GLOW, config::glow)
            create(RenderType.GLOBAL, config::global)
        }
        val texture by groups.registering {
            val config = TextureConfig()
            fun create(default: Boolean, reload: Boolean, prop: KMutableProperty0<Boolean>) = options.registering {
                name(TextureConfig.text(prop))
                tooltip(TextureConfig.tooltip(prop))
                binding(prop, default)
                controller(TickBoxControllerBuilder::create)
                if (reload) flag(OptionFlag.ASSET_RELOAD)
            }
            create(default = true, reload = true, prop = config::cropTrims)
            create(default = false, reload = true, prop = config::useDarkerTrim)
            create(default = false, reload = true, prop = config::useBannerTextures)
            create(default = true, reload = false, prop = config::useElytraModel)
        }
    }
    save(ETClientConfig.Companion::save)
}.generateScreen(parent)