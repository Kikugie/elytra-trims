package dev.kikugie.elytratrims.common.recipe

import net.minecraft.item.ItemStack
import net.minecraft.recipe.ShapelessRecipe
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList
import net.minecraft.world.World

abstract class DelegatedRecipe(
    @JvmField val id: Identifier,
    @JvmField val output: ItemStack,
) : ShapelessRecipe(/*? if <1.21 >>*/id, "impossible", CraftingRecipeCategory.EQUIPMENT, output, DefaultedList.of()) {
    abstract fun matches(input: Stacks): Boolean
    abstract fun craft(input: Stacks): ItemStack

    //? if >=1.21 {
    /*override fun craft(input: net.minecraft.recipe.input.CraftingRecipeInput, lookup: net.minecraft.registry.RegistryWrapper.WrapperLookup): ItemStack =
        craft(input.stacks)
    override fun matches(input: net.minecraft.recipe.input.CraftingRecipeInput, world: World): Boolean =
        matches(input.stacks)
    *///?} else {
    override fun craft(inventory: net.minecraft.inventory.RecipeInputInventory, lookup: net.minecraft.registry.DynamicRegistryManager): ItemStack =
        craft(inventory.inputStacks)
    override fun matches(inventory: net.minecraft.inventory.RecipeInputInventory, world: World): Boolean =
        matches(inventory.inputStacks)
    //?}
}