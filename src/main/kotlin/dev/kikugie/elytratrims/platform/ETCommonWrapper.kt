package dev.kikugie.elytratrims.platform

import dev.kikugie.elytratrims.common.*
import dev.kikugie.elytratrims.common.recipe.*
import net.minecraft.recipe.RecipeSerializer

/*? if fabric {*/
object ETCommonWrapper : net.fabricmc.api.ModInitializer {
    override fun onInitialize() {
        ETCommon.init()

        RecipeSerializer.register(
            "elytratrims:crafting_special_elytrapatterns",
            ETPatternRecipe.SERIALIZER
        )
        RecipeSerializer.register(
            "elytratrims:crafting_special_elytraglow",
            ETGlowRecipe.SERIALIZER
        )
        RecipeSerializer.register(
            "elytratrims:crafting_special_elytraanimation",
            ETAnimationRecipe.SERIALIZER
        )
    }
}
/*?} elif forge {*/
/*import dev.kikugie.elytratrims.client.ETClient
import dev.kikugie.elytratrims.client.config.lib.create
import dev.kikugie.elytratrims.client.config.lib.isAvailable
import dev.kikugie.elytratrims.client.resource.ETAtlasHolder
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.runWhenOn

@Mod(ETReference.MOD_ID)
object ETCommonWrapper {
    init {
        ETCommon.init()
        runWhenOn(Dist.CLIENT) {
            ETClient.init()
            MOD_BUS.addListener<RegisterClientReloadListenersEvent> {
                it.registerReloadListener(ETAtlasHolder)
            }
            if (isAvailable) ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenFactory::class.java,
            ) { ConfigScreenFactory { _, parent -> create(parent) } }
        }
        val registry = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ETReference.MOD_ID)
        registry.register(
            "crafting_special_elytrapatterns"
        ) { ETPatternRecipe.SERIALIZER }
        registry.register(
            "crafting_special_elytraglow"
        ) { ETGlowRecipe.SERIALIZER }
        registry.register(
            "crafting_special_elytraanimation"
        ) { ETAnimationRecipe.SERIALIZER }
        registry.register(MOD_BUS)
    }

}
*//*?} else {*/
/*import dev.kikugie.elytratrims.client.ETClient
import dev.kikugie.elytratrims.client.config.lib.create
import dev.kikugie.elytratrims.client.config.lib.isAvailable
import dev.kikugie.elytratrims.client.resource.ETAtlasHolder
import net.minecraft.registry.Registries
import net.neoforged.fml.common.Mod
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.ModLoadingContext
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.runWhenOn
import java.util.function.Supplier

//? if <1.20.6 {
/^typealias CSF = net.neoforged.neoforge.client.ConfigScreenHandler.ConfigScreenFactory^/
//?} else
typealias CSF = net.neoforged.neoforge.client.gui.IConfigScreenFactory

@Mod(ETReference.MOD_ID)
object ETCommonWrapper {
    init {
        ETCommon.init()
        runWhenOn(Dist.CLIENT) {
            ETClient.init()
            MOD_BUS.addListener<RegisterClientReloadListenersEvent> {
                it.registerReloadListener(ETAtlasHolder)
            }

            if (isAvailable) ModLoadingContext.get().registerExtensionPoint(
                CSF::class.java,
            ) { CSF { _, parent -> create(parent) } }
        }

        val registry = DeferredRegister.create(Registries.RECIPE_SERIALIZER, ETReference.MOD_ID)
        registry.register(
            "crafting_special_elytrapatterns",
            Supplier { ETPatternRecipe.SERIALIZER }
        )
        registry.register(
            "crafting_special_elytraglow",
            Supplier { ETGlowRecipe.SERIALIZER }
        )
        registry.register(
            "crafting_special_elytraanimation",
            Supplier { ETAnimationRecipe.SERIALIZER }
        )
        registry.register(MOD_BUS)
    }
}
  *//*?}*/