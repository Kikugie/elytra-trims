package dev.kikugie.elytratrims.common.recipe

import dev.kikugie.elytratrims.common.ETCommon
import dev.kikugie.elytratrims.common.util.identifier
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.recipe.*
import net.minecraft.util.Identifier

internal typealias Stacks = Iterable<ItemStack>

internal inline fun Stacks.firstItemCopy(filter: (Item) -> Boolean): ItemStack?
    = firstOrNull { filter(it.item) }?.copy()

typealias RecipeWrapper =
/*? if <1.20.2*/Recipe<*>
/*? if >=1.20.2*//*RecipeEntry<out Recipe<*>>*/

@JvmField
val MOD_RECIPES: Map<Identifier, DelegatedRecipe> = listOf(
    ETPatternRecipe(identifier("elytratrims:patterns")),
    ETGlowRecipe(identifier("elytratrims:glow")),
    ETAnimationRecipe(identifier("elytratrims:animation"))
).associateBy { it.id }

//? if <1.21 {
fun filterRecipes(elements: Collection<RecipeWrapper>): Collection<RecipeWrapper> =
    if (ETCommon.config.requireClientSide) elements else elements.map { wrapper ->
        MOD_RECIPES[wrapper.id] ?: wrapper
    }
//?} else {
/*fun filterRecipes(elements: Collection<RecipeWrapper>): Collection<RecipeWrapper> =
    if (ETCommon.config.requireClientSide) elements else elements.map { wrapper ->
        MOD_RECIPES[wrapper.id]?.let { RecipeEntry(it.id, it) } ?: wrapper
    }
*///?}