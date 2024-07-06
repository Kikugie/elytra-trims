package dev.kikugie.elytratrims.common.compat

import dev.kikugie.elytratrims.platform.ModStatus

internal inline fun <T> runWithMod(mod: String, action: () -> T): T? =
    if (ModStatus.isLoaded(mod)) action() else null