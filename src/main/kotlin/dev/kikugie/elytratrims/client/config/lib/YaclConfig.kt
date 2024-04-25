package dev.kikugie.elytratrims.client.config.lib

import dev.isxander.yacl3.api.*
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import dev.isxander.yacl3.gui.controllers.cycling.EnumController
import dev.kikugie.elytratrims.client.ETClient.config
import dev.kikugie.elytratrims.client.config.*
import dev.kikugie.elytratrims.client.translation
import net.minecraft.client.gui.screen.Screen
import dev.kikugie.elytratrims.client.config.Option as ETOption

object YaclConfig {
    fun create(parent: Screen?): Screen {
        return YetAnotherConfigLib.createBuilder()
            .title("elytratrims.config.title".translation())
            .category(
                ConfigCategory.createBuilder()
                    .name("elytratrims.config.category".translation())
                    .group(
                        OptionGroup.createBuilder()
                            .name("elytratrims.config.category.render".translation())
                            .apply {
                                RenderType.entries.forEach {
                                    option(config.render[it].yaclOption())
                                }
                            }
                            .build()
                    )
                    .group(
                        OptionGroup.createBuilder()
                            .name("elytratrims.config.category.texture".translation())
                            .option(config.texture.cropTrims.yaclOption(true))
                            .option(config.texture.useDarkerTrim.yaclOption(true))
                            .option(config.texture.useBannerTextures.yaclOption(true))
                            .option(config.texture.useElytraModel.yaclOption(false))
                            .build()
                    )
                    .build()
            )
            .save(config::save)
            .build()
            .generateScreen(parent)
    }

    private fun TextureOption.yaclOption(reload: Boolean) = yaclBuilder()
        .controller(TickBoxControllerBuilder::create)
        .apply { if (reload) flag(OptionFlag.ASSET_RELOAD) }
        .build()

    private fun RenderModeOption.yaclOption() = yaclBuilder()
        .customController { EnumController(it, RenderMode::name, RenderMode.entries.toTypedArray()) }
        .build()

    private fun <T : Any> ETOption<T>.yaclBuilder() = Option.createBuilder<T>()
        .name(name())
        .description(OptionDescription.of(tooltip()))
        .binding(default, { value }, { value = it })
}