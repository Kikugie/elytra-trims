package dev.kikugie.elytratrims.common.recipe

import dev.kikugie.elytratrims.common.ETReference
import dev.kikugie.elytratrims.common.access.FeatureAccess.getBaseColor
import dev.kikugie.elytratrims.common.access.FeatureAccess.getPatterns
import dev.kikugie.elytratrims.common.access.FeatureAccess.setColor
import dev.kikugie.elytratrims.common.access.FeatureAccess.setPatterns
import dev.kikugie.elytratrims.common.util.isProbablyElytra
import net.minecraft.item.BannerItem
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.util.Identifier

class ETPatternRecipe(id: Identifier, category: CraftingRecipeCategory) : DelegatedRecipe(id, category) {
    override fun matches(input: Stacks): Boolean {
        var elytra = 0
        var banner = 0
        input.forEach {
            if (isProbablyElytra(it.item)) elytra++
            else if (it.item is BannerItem) {
                if (it.getPatterns().isEmpty()) return false
                banner++
            } else if (!it.isEmpty) return false
            if (elytra > 1 || banner > 1) return false
        }
        return elytra == 1 && banner == 1
    }

    override fun craft(input: Stacks): ItemStack {
        val banner = input.firstItemCopy { it is BannerItem } ?: return ItemStack.EMPTY
        val elytra = input.firstItemCopy(::isProbablyElytra) ?: return ItemStack.EMPTY
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
        val SERIALIZER: RecipeSerializer<ETPatternRecipe> =
            serializer(ETReference.id("crafting_special_elytrapatterns"), ::ETPatternRecipe)
    }
}