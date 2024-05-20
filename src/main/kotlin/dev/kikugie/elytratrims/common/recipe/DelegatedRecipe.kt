package dev.kikugie.elytratrims.common.recipe

import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.SpecialCraftingRecipe
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.util.Identifier
import net.minecraft.world.World

abstract class DelegatedRecipe(id: Identifier, category: CraftingRecipeCategory) :
    /*? if <1.20.2 */SpecialCraftingRecipe(id, category)
    /*? if >=1.20.2 *//*SpecialCraftingRecipe(category)*/  
{
    abstract fun matches(inventory: Inventory): Boolean
    abstract fun craft(inventory: Inventory): ItemStack

    /*? >1.20.4 {*/
    /*override fun craft(inventory: net.minecraft.inventory.RecipeInputInventory, lookup: net.minecraft.registry.RegistryWrapper.WrapperLookup): ItemStack {
        return craft(inventory)
    }
      *//*?} elif >=1.20 {*/
    override fun craft(inventory: net.minecraft.inventory.RecipeInputInventory, lookup: net.minecraft.registry.DynamicRegistryManager): ItemStack {
        return craft(inventory)
    }
    /*?} else {*/
    /*override fun craft(inventory: net.minecraft.inventory.CraftingInventory, registryManager: net.minecraft.registry.DynamicRegistryManager): ItemStack {
        return craft(inventory)
    }
      *//*?}*/

    /*? <1.20 {*/
    /*override fun matches(inventory: net.minecraft.inventory.CraftingInventory, world: World): Boolean {
        return matches(inventory)
    }
      *//*?} else {*/
    override fun matches(inventory: net.minecraft.inventory.RecipeInputInventory, world: World): Boolean {
        return matches(inventory)
    }
    /*?}*/
}