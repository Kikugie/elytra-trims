package dev.kikugie.elytratrims.common

import dev.kikugie.elytratrims.platform.ModStatus
import java.util.*

object ETCommentary {
    fun run() {
        print("betterend") { "IHATEBCLIB ".repeat(Random().nextInt(20, 100)) }
        print("dashloader") { "DashLoader, what do you mean all textures already exist? This is not how we do things here!" }
        print("minecraftcapes") { "MinecraftCapes, would you like me to teach you how to write mixins? For free, even." }
        print("optifine") { "*Metal pipe sound effect*" } // Forge moment
        print("optifabric") { "*Metal pipe sound effect*" } // In case dependency overrides are applied
        print("showmeyourskin") { "No, you can't see my skin!" }
        print("allthetrims") { "All the RGB!" }
        print("more-armor-trims") { "Even more trims?! Hell yea!" }
        print("stacked-armor-trims") { "Making trims 64-stackable!" }
    }

    inline fun print(mod: String, message: () -> String) {
        if (ModStatus.isLoaded(mod)) ETReference.LOGGER.info(message())
    }
}