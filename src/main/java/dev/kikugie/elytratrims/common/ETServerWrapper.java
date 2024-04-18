package dev.kikugie.elytratrims.common;

import dev.kikugie.elytratrims.common.config.ServerConfigs;
import dev.kikugie.elytratrims.common.recipe.ETGlowRecipe;
import dev.kikugie.elytratrims.common.recipe.ETPatternRecipe;
import net.minecraft.item.Item;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

/*? if fabric {*/
public class ETServerWrapper implements net.fabricmc.api.ModInitializer {
    @Override
    public void onInitialize() {
        ETServer.init();

        if (ServerConfigs.getConfig().addPatterns)
            RecipeSerializer.register("crafting_special_elytrapatterns", ETPatternRecipe.SERIALIZER);
        if (ServerConfigs.getConfig().addGlow)
            RecipeSerializer.register("crafting_special_elytraglow", ETGlowRecipe.SERIALIZER);
    }

    public static Identifier getItemId(Item item) {
        return Registries.ITEM.getId(item);
    }
}
/*?} elif forge {*//*
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(ETReference.MOD_ID)
public class ETServerWrapper {
    public ETServerWrapper() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ETClientWrapper::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ETServerWrapper::init);

        DeferredRegister<RecipeSerializer<?>> EVENT = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "minecraft");

        if (ServerConfigs.getConfig().addPatterns)
            EVENT.register("crafting_special_elytrapatterns", () -> ETRecipeSerializers.ELYTRA_PATTERNS);
        if (ServerConfigs.getConfig().addGlow)
            EVENT.register("crafting_special_elytraglow", () -> ETRecipeSerializers.ELYTRA_GLOW);
        EVENT.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void init(FMLCommonSetupEvent event) {
        ETServer.init();
    }

    public static Identifier getItemId(Item item) {
        return ForgeRegistries.ITEMS.getKey(item);
    }
}
*//*?} else {*//*
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(ETReference.MOD_ID)
public class ETServerWrapper {
    public ETServerWrapper(IEventBus bus) {
        bus.addListener(ETClientWrapper::init);
        bus.addListener(ETServerWrapper::init);

        DeferredRegister<RecipeSerializer<?>> EVENT = DeferredRegister.create(Registries.RECIPE_SERIALIZER, "minecraft");

        if (ServerConfigs.getConfig().addPatterns)
            EVENT.register("crafting_special_elytrapatterns", () -> ETRecipeSerializers.ELYTRA_PATTERNS);
        if (ServerConfigs.getConfig().addGlow)
            EVENT.register("crafting_special_elytraglow", () -> ETRecipeSerializers.ELYTRA_GLOW);
        EVENT.register(bus);
    }

    public static void init(FMLCommonSetupEvent event) {
        ETServer.init();
    }

    public static Identifier getItemId(Item item) {
        return Registries.ITEM.getId(item);
    }
}
*//*?}*/