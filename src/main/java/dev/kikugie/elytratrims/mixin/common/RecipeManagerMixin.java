package dev.kikugie.elytratrims.mixin.common;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import dev.kikugie.elytratrims.common.recipe.RecipeUtilsKt;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @ModifyReceiver(
            method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V",
            at = @At(value = "INVOKE", target = "Ljava/util/Map;entrySet()Ljava/util/Set;", ordinal = 1))
    private <T extends Map<RecipeType<?>, ImmutableMap.Builder<Identifier, Recipe<?>>>> T addElytraRecipes(T instance) {
        RecipeUtilsKt.addElytraRecipes(instance.get(RecipeType.CRAFTING));
        return instance;
    }

    @ModifyReceiver(
            method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V",
            at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;"))
    private <T extends ImmutableMap.Builder<Identifier, Recipe<?>>> T addElytraRecipesElectricBoogaloo(T instance) {
        RecipeUtilsKt.addElytraRecipes(instance);
        return instance;
    }
}
