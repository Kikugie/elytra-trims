package dev.kikugie.elytratrims.mixin.common;

import com.google.common.collect.Iterables;
import dev.kikugie.elytratrims.common.ETCommon;
import dev.kikugie.elytratrims.common.config.RequireClientTester;
import dev.kikugie.elytratrims.common.recipe.DelegatedRecipe;
import dev.kikugie.elytratrims.mixin.plugin.MixinConfigurable;
import dev.kikugie.elytratrims.mixin.plugin.RequireTest;
import net.minecraft.network.packet.s2c.play.SynchronizeRecipesS2CPacket;
import net.minecraft.recipe.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * Hides recipes from clients, because they use custom serializers and would cause registry desync.
 * The recipes are still available, but not present in the recipe book.
 */
@RequireTest(RequireClientTester.class)
@MixinConfigurable
@Mixin(value = SynchronizeRecipesS2CPacket.class, remap = false)
public abstract class SynchronizeRecipesS2CPacketMixin {

    /*? if <1.20.2 {*/
    @ModifyArg(method = "<init>(Ljava/util/Collection;)V", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Lists;newArrayList(Ljava/lang/Iterable;)Ljava/util/ArrayList;"))
    private Iterable<Recipe<?>> removeElytraPatternRecipe(Iterable<Recipe<?>> elements) {
        if (ETCommon.config.getRequireClientSide())
            return elements;
        return Iterables.filter(elements, recipe -> !(recipe instanceof DelegatedRecipe));
    }
    /*?} elif <=1.20.4 {*//*
    @ModifyArg(method = "<init>(Ljava/util/Collection;)V", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Lists;newArrayList(Ljava/lang/Iterable;)Ljava/util/ArrayList;"))
    private Iterable<net.minecraft.recipe.RecipeEntry<? extends Recipe<?>>> removeElytraPatternRecipe(Iterable<net.minecraft.recipe.RecipeEntry<? extends Recipe<?>>> elements) {
        if (ETCommon.config.getRequireClientSide())
            return elements;
        return Iterables.filter(elements, entry -> {
            Recipe<?> recipe = entry.value();
            return !(recipe instanceof DelegatedRecipe);
        });
    }
    *//*?} else {*//*
    @ModifyArg(method = "<init>(Ljava/util/Collection;)V", at = @At(value = "INVOKE", target = "Ljava/util/List;copyOf(Ljava/util/Collection;)Ljava/util/List;"))
    private Collection<net.minecraft.recipe.RecipeEntry<? extends Recipe<?>>> removeElytraPatternRecipe(Collection<net.minecraft.recipe.RecipeEntry<? extends Recipe<?>>> elements) {
        if (ETCommon.config.getRequireClientSide())
            return elements;
        Collection<net.minecraft.recipe.RecipeEntry<? extends Recipe<?>>> res = new ArrayList<>();
        for (net.minecraft.recipe.RecipeEntry<? extends Recipe<?>> entry : elements) {
            Recipe<?> recipe = entry.value();
            if (!(recipe instanceof DelegatedRecipe))
                res.add(entry);
        }
        return res;
    }
    *//*?} */
}
