package dev.kikugie.elytratrims.common.recipe

import dev.kikugie.elytratrims.api.ElytraTrimsAPI
import dev.kikugie.elytratrims.common.access.FeatureAccess.addGlow
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.util.Identifier

class ETGlowRecipe(id: Identifier) : DelegatedRecipe(id, SAMPLE)  {
    override fun matches(input: Stacks): Boolean {
        var item = 0
        var sac = 0
        input.forEach {
            if (ElytraTrimsAPI.isProbablyElytra(it)) item++
            else if (it.item == Items.GLOW_INK_SAC) sac++
            else if (!it.isEmpty) return false
            if (item > 1 || sac > 1) return false
        }
        return item == 1 && sac == 1
    }

    override fun craft(input: Stacks): ItemStack {
        val elytra = input.firstItemCopy(ElytraTrimsAPI::isProbablyElytra) ?: return ItemStack.EMPTY
        elytra.addGlow()
        return elytra
    }

    override fun fits(width: Int, height: Int): Boolean = width * height >= 2

    companion object {
        val SAMPLE = ItemStack(Items.ELYTRA).apply { addGlow() }
    }
}