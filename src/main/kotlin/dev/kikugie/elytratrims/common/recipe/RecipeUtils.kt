package dev.kikugie.elytratrims.common.recipe

import com.google.common.collect.ImmutableMap.Builder
import dev.kikugie.elytratrims.common.ETCommon
import dev.kikugie.elytratrims.common.util.identifier
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.recipe.*
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.util.Identifier

internal inline fun Stacks.firstItemCopy(filter: (Item) -> Boolean): ItemStack?
    = firstOrNull { filter(it.item) }?.copy()

internal fun <T : CraftingRecipe> serializer(defaultId: Identifier, init: (Identifier, CraftingRecipeCategory) -> T): SpecialRecipeSerializer<T> =
    /*? if <1.20.2*/SpecialRecipeSerializer(init)
    /*? if >=1.20.2*//*SpecialRecipeSerializer { init(defaultId, it) }*/  

typealias RecipeWrapper =
/*? if <1.20.2*/Recipe<*>
/*? if >=1.20.2*//*RecipeEntry<out Recipe<*>>*/  

fun filterRecipes(elements: Collection<RecipeWrapper>): Collection<RecipeWrapper> =
    if (ETCommon.config.requireClientSide) elements else elements.filter {
        it/*? if >=1.20.2 {*//*.value  *//*?}*/ !is DelegatedRecipe
    }

private val animation = identifier("elytratrims_animation").let {
    it to ETAnimationRecipe(it, CraftingRecipeCategory.EQUIPMENT)
}
private val patterns = identifier("elytratrims_patterns").let {
    it to ETPatternRecipe(it, CraftingRecipeCategory.EQUIPMENT)
}
private val glow = identifier("elytratrims_glow").let {
    it to ETGlowRecipe(it, CraftingRecipeCategory.EQUIPMENT)
}

fun Builder<Identifier, Recipe<*>>.addElytraRecipes() {
    animation.run { put(first, second) }
    patterns.run { put(first, second) }
    glow.run { put(first, second) }
}