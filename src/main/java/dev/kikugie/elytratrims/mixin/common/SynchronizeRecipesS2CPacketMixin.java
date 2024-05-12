package dev.kikugie.elytratrims.mixin.common;

import dev.kikugie.elytratrims.common.config.RequireClientTester;
import dev.kikugie.elytratrims.common.recipe.RecipeUtilsKt;
import dev.kikugie.elytratrims.mixin.plugin.MixinConfigurable;
import dev.kikugie.elytratrims.mixin.plugin.RequireTest;
import net.minecraft.network.packet.s2c.play.SynchronizeRecipesS2CPacket;
import net.minecraft.recipe.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Collection;

/**
 * Hides recipes from clients, because they use custom serializers and would cause registry desync.
 * The recipes are still available, but not present in the recipe book.
 */
@RequireTest(RequireClientTester.class)
@MixinConfigurable
@Mixin(value = SynchronizeRecipesS2CPacket.class, remap = false)
public abstract class SynchronizeRecipesS2CPacketMixin {
    /*? if <1.20.2 {*/
    @ModifyVariable(method = "<init>(Ljava/util/Collection;)V", at = @At("HEAD"), argsOnly = true)
    private static Collection<Recipe<?>> removeElytraPatternRecipe(Collection<Recipe<?>> elements) {
        return RecipeUtilsKt.filterRecipes(elements);
    }
    /*?} else {*//*
    @ModifyVariable(method = "<init>(Ljava/util/Collection;)V", at = @At("HEAD"), argsOnly = true)
    private static Collection<net.minecraft.recipe.RecipeEntry<? extends Recipe<?>>> removeElytraPatternRecipe(Collection<net.minecraft.recipe.RecipeEntry<? extends Recipe<?>>> elements) {
        return RecipeUtilsKt.filterRecipes(elements);
    }
    *//*?} */
}
