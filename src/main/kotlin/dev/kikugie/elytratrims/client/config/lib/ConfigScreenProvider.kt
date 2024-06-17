package dev.kikugie.elytratrims.client.config.lib

import dev.kikugie.elytratrims.platform.ModStatus

val isAvailable get() = ModStatus.isLoaded("yet_another_config_lib_v3")