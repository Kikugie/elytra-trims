package dev.kikugie.elytratrims.common.recipe

import dev.kikugie.elytratrims.api.ElytraTrimsAPI
import dev.kikugie.elytratrims.common.access.FeatureAccess.getBaseColor
import dev.kikugie.elytratrims.common.access.FeatureAccess.getPatterns
import dev.kikugie.elytratrims.common.access.FeatureAccess.setColor
import dev.kikugie.elytratrims.common.access.FeatureAccess.setPatterns
import net.minecraft.item.BannerItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.util.Identifier

class ETPatternRecipe(id: Identifier) : DelegatedRecipe(id, SAMPLE)  {
    override fun matches(input: Stacks): Boolean {
        var elytra = 0
        var banner = 0
        input.forEach {
            if (ElytraTrimsAPI.isProbablyElytra(it)) elytra++
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
        val elytra = input.firstItemCopy(ElytraTrimsAPI::isProbablyElytra) ?: return ItemStack.EMPTY
        elytra.setPatterns(banner)
        val color = banner.getBaseColor()
        if (color != 0) elytra.setColor(color)
        return elytra
    }

    override fun fits(width: Int, height: Int): Boolean {
        return width * height >= 2
    }

    companion object {
        val SAMPLE = ItemStack(Items.ELYTRA).apply {
            // TODO
        }
    }
}