package dev.kikugie.elytratrims.common.recipe

import dev.kikugie.elytratrims.common.ETCommon
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.recipe.*

internal typealias Stacks = Iterable<ItemStack>

internal inline fun Stacks.firstItemCopy(filter: (Item) -> Boolean): ItemStack?
    = firstOrNull { filter(it.item) }?.copy()

typealias RecipeWrapper =
/*? if <1.20.2*/Recipe<*>
/*? if >=1.20.2*//*RecipeEntry<out Recipe<*>>*/  

fun filterRecipes(elements: Collection<RecipeWrapper>): Collection<RecipeWrapper> =
    if (ETCommon.config.requireClientSide) elements else elements.map {
        val recipe = it/*? if >=1.20.2 {*//*.value  *//*?}*/
        if (recipe !is DelegatedRecipe) it
        /*? if <1.21 {*/else recipe.replacement
        /*?} else*//*else RecipeEntry(recipe.id, recipe.replacement)*/
    }