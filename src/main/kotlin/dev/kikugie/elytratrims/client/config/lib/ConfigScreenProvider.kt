package dev.kikugie.elytratrims.client.config.lib

import dev.kikugie.elytratrims.client.translation
import dev.kikugie.elytratrims.platform.ModStatus
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ConfirmScreen
import net.minecraft.client.gui.screen.Screen
import net.minecraft.util.Util
import java.net.URI

object ConfigScreenProvider {
    val isAvailable = ModStatus.isLoaded("yet_another_config_lib_v3")

    fun open(parent: Screen?) = if (isAvailable) YaclConfig.create(parent) else createDummyScreen(parent)

    fun createDummyScreen(parent: Screen?) = ConfirmScreen(
        {
            if (it) Util.getOperatingSystem().open(
                URI.create(
                    "https://modrinth.com/mod/yacl/versions?l=${ModStatus.platform}&g=${ModStatus.mcVersion}"
                )
            ) else MinecraftClient.getInstance().setScreen(parent)
        },
        "elytratrims.config.noyacl".translation(),
        "elytratrims.config.noyacl.message".translation()
    )
}