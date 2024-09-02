package dev.kikugie.elytratrims.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.elytratrims.common.recipe.RecipeUtilsKt;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
	//? if <1.21 {
	@ModifyExpressionValue(
		method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/recipe/RecipeManager;deserialize(Lnet/minecraft/util/Identifier;Lcom/google/gson/JsonObject;)Lnet/minecraft/recipe/Recipe;"
		)
	)
	private Recipe<?> modifyElytraRecipes(Recipe<?> original, @Local Identifier id) {
		return Objects.requireNonNullElse(RecipeUtilsKt.MOD_RECIPES.get(id), original);
	}
	//?} else {
	/*@ModifyExpressionValue(
		method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V",
		at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/DataResult;getOrThrow(Ljava/util/function/Function;)Ljava/lang/Object;")
	)
	private Object modifyElytraRecipes(Object original, @Local Identifier id) {
		return Objects.requireNonNullElse(RecipeUtilsKt.MOD_RECIPES.get(id), original);
	}
	*///?}
}
