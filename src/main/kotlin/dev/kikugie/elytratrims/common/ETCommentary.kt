package dev.kikugie.elytratrims.common

import dev.kikugie.elytratrims.platform.ModStatus
import java.util.*

object ETCommentary {
    fun run() {
        print("betterend") { "IHATEBCLIB ".repeat(Random().nextInt(20, 100)) }
    }

    inline fun print(mod: String, message: () -> String) {
        if (ModStatus.isLoaded(mod)) ETReference.LOGGER.info(message())
    }
}