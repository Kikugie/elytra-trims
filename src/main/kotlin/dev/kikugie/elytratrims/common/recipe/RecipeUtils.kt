package dev.kikugie.elytratrims.common.recipe

import net.minecraft.inventory.Inventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.recipe.CraftingRecipe
import net.minecraft.recipe.SpecialRecipeSerializer
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.util.Identifier

internal fun Inventory.sequence(): Sequence<ItemStack> = sequence {
    for (i in 0 until size()) yield(getStack(i))
}

internal fun Inventory.firstItem(predicate: (Item) -> Boolean) = sequence().firstOrNull {
    predicate(it.item)
}

internal fun <T : CraftingRecipe> serializer(defaultId: Identifier, init: (Identifier, CraftingRecipeCategory) -> T): SpecialRecipeSerializer<T> =
    /*? if <1.20.2*/SpecialRecipeSerializer(init)
    /*? if >=1.20.2*//*SpecialRecipeSerializer { init(defaultId, it) }*/