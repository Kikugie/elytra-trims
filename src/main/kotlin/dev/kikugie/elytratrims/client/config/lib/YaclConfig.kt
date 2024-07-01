package dev.kikugie.elytratrims.client.config.lib

import dev.isxander.yacl3.api.OptionDescription
import dev.isxander.yacl3.api.OptionFlag
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import dev.isxander.yacl3.dsl.YetAnotherConfigLib
import dev.isxander.yacl3.dsl.binding
import dev.isxander.yacl3.gui.controllers.cycling.EnumController
import dev.kikugie.elytratrims.client.ETClient
import dev.kikugie.elytratrims.client.config.*
import dev.kikugie.elytratrims.client.translation
import dev.kikugie.elytratrims.common.ETReference
import net.minecraft.client.gui.screen.Screen
import kotlin.reflect.KMutableProperty0

object YaclConfig {
    fun create(parent: Screen?): Screen = YetAnotherConfigLib(ETReference.MOD_ID) {
        title("elytratrims.config.title".translation())
        val main by categories.registering {
            name("elytratrims.config.category".translation())
            val render by groups.registering {
                val config = ETClient.config.render
                name("elytratrims.config.category.render".translation())
                fun create(type: RenderType, prop: KMutableProperty0<RenderMode>) = options.register<RenderMode>(prop.name) {
                    name(type.text)
                    description(OptionDescription.of(type.tooltip))
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
                val config = ETClient.config.texture
                name("elytratrims.config.category.texture".translation())
                fun create(default: Boolean, reload: Boolean, prop: KMutableProperty0<Boolean>) = options.register<Boolean>(prop.name) {
                    name(TextureConfig.text(prop))
                    description(OptionDescription.of(TextureConfig.tooltip(prop)))
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
}