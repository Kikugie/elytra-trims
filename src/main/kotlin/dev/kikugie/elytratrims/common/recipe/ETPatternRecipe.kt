package dev.kikugie.elytratrims.common.recipe

import dev.kikugie.elytratrims.common.access.FeatureAccess.getBaseColor
import dev.kikugie.elytratrims.common.access.FeatureAccess.getPatterns
import dev.kikugie.elytratrims.common.access.FeatureAccess.setColor
import dev.kikugie.elytratrims.common.access.FeatureAccess.setPatterns
import dev.kikugie.elytratrims.common.util.isProbablyElytra
import net.minecraft.inventory.Inventory
import net.minecraft.item.BannerItem
import net.minecraft.item.ItemStack
import net.minecraft.recipe.SpecialRecipeSerializer
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.util.Identifier

class ETPatternRecipe(id: Identifier, category: CraftingRecipeCategory) : DelegatedRecipe(id, category) {
    override fun matches(inventory: Inventory): Boolean {
        var elytra = 0
        var banner = 0
        for (slot in 0 until inventory.size()) {
            val stack = inventory.getStack(slot)
            if (isProbablyElytra(stack.item)) elytra++
            else if (stack.item is BannerItem) {
                if (stack.getPatterns().isEmpty()) return false
                banner++
            } else if (!stack.isEmpty) return false
            if (elytra > 1 || banner > 1) return false
        }
        return elytra == 1 && banner == 1
    }

    override fun craft(inventory: Inventory): ItemStack {
        var elytra = ItemStack.EMPTY
        var banner = ItemStack.EMPTY
        for (slot in 0 until inventory.size()) {
            val stack = inventory.getStack(slot)
            if (stack.isEmpty) continue
            if (isProbablyElytra(stack.item)) elytra = stack.copy()
            else if (stack.item is BannerItem) banner = stack
        }
        elytra.setPatterns(banner)
        val color = banner.getBaseColor()
        if (color != 0) elytra.setColor(color)
        return elytra
    }

    override fun fits(width: Int, height: Int): Boolean {
        return width * height >= 2
    }

    override fun getSerializer() = SERIALIZER

    companion object {
        var SERIALIZER =
            /*? if <1.20.2*/SpecialRecipeSerializer { id, category -> ETPatternRecipe(id, category) }
            /*? if >=1.20.2*//*SpecialRecipeSerializer { ETPatternRecipe(Identifier("crafting_special_elytrapatterns"), it) }*/
    }
}