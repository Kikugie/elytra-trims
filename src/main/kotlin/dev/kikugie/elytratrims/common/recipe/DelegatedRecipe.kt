package dev.kikugie.elytratrims.common.recipe

import net.minecraft.item.ItemStack
import net.minecraft.recipe.SpecialCraftingRecipe
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.util.Identifier
import net.minecraft.world.World

internal typealias Stacks = Iterable<ItemStack>

abstract class DelegatedRecipe(id: Identifier, category: CraftingRecipeCategory) :
    /*? if <1.20.2*/SpecialCraftingRecipe(id, category)
    /*? if >=1.20.2*//*SpecialCraftingRecipe(category)*/  
{
    abstract fun matches(input: Stacks): Boolean
    abstract fun craft(input: Stacks): ItemStack

    //? if >=1.21 {
    /*override fun craft(input: net.minecraft.recipe.input.CraftingRecipeInput, lookup: net.minecraft.registry.RegistryWrapper.WrapperLookup): ItemStack =
        craft(input.stacks)
    override fun matches(input: net.minecraft.recipe.input.CraftingRecipeInput, world: World): Boolean =
        matches(input.stacks)
    *///?} elif >=1.20.6 {
    /*override fun craft(inventory: net.minecraft.inventory.RecipeInputInventory, lookup: net.minecraft.registry.RegistryWrapper.WrapperLookup): ItemStack =
        craft(inventory.heldStacks)
    override fun matches(inventory: net.minecraft.inventory.RecipeInputInventory, world: World): Boolean =
        matches(inventory.heldStacks)
    *///?} elif >=1.20 {
    override fun craft(inventory: net.minecraft.inventory.RecipeInputInventory, lookup: net.minecraft.registry.DynamicRegistryManager): ItemStack =
        craft(inventory.inputStacks)
    override fun matches(inventory: net.minecraft.inventory.RecipeInputInventory, world: World): Boolean =
        matches(inventory.heldStacks)
    //?} else {
    /*override fun craft(inventory: net.minecraft.inventory.CraftingInventory, registryManager: net.minecraft.registry.DynamicRegistryManager): ItemStack =
        craft(inventory)
    override fun matches(inventory: net.minecraft.inventory.CraftingInventory, world: World): Boolean =
        matches(inventory)
    *///?}
}