package dev.kikugie.elytratrims.common.compat

import dev.kikugie.elytratrims.platform.ModStatus
import dev.tr7zw.firstperson.FirstPersonModelCore

object FirstPersonModelCompat {
    inline fun runWithFirstPerson(action: () -> Unit) {
        if (!ModStatus.isLoaded("firstperson")) action()
        else {
            val instance = FirstPersonModelCore.instance
            val rendering = instance.isRenderingPlayer
            action()
            instance.isRenderingPlayer = rendering
        }
    }
}