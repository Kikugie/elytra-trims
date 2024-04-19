package dev.kikugie.elytratrims.client.config.lib

import dev.kikugie.elytratrims.platform.ModStatus
import net.minecraft.client.gui.screen.Screen

object ConfigScreenProvider {
    val screen: ((Screen?) -> Screen)? =
        if (ModStatus.isLoaded("yet_another_config_lib_v3")) YaclConfig::create
    else null
}