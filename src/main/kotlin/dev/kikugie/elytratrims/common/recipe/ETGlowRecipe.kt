package dev.kikugie.elytratrims.common.recipe

import dev.kikugie.elytratrims.common.access.FeatureAccess.addGlow
import dev.kikugie.elytratrims.common.util.isProbablyElytra
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.SpecialRecipeSerializer
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.util.Identifier

class ETGlowRecipe(id: Identifier, category: CraftingRecipeCategory) : DelegatedRecipe(id, category) {
    override fun matches(inventory: Inventory): Boolean {
        var item = 0
        var sac = 0
        for (slot in 0 until inventory.size()) {
            val stack = inventory.getStack(slot)
            if (isProbablyElytra(stack.item)) item++
            else if (stack.item === Items.GLOW_INK_SAC) sac++
            else if (!stack.isEmpty) return false
            if (item > 1 || sac > 1) return false
        }
        return item == 1 && sac == 1
    }

    override fun craft(inventory: Inventory): ItemStack {
        var item = ItemStack.EMPTY
        for (slot in 0 until inventory.size()) {
            val stack = inventory.getStack(slot)
            if (!isProbablyElytra(stack.item)) continue
            item = stack.copy()
        }
        item.addGlow()
        return item
    }

    override fun fits(width: Int, height: Int): Boolean = width * height >= 2

    override fun getSerializer() = SERIALIZER

    companion object {
        val SERIALIZER: RecipeSerializer<ETGlowRecipe> =
            /*? if <1.20.2*/SpecialRecipeSerializer { id, category -> ETGlowRecipe(id, category) }
            /*? if >=1.20.2*//*SpecialRecipeSerializer { ETGlowRecipe(Identifier("crafting_special_elytraglow"), it) }*/
    }
}