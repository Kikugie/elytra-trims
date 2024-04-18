package dev.kikugie.elytratrims.common.recipe;

import dev.kikugie.elytratrims.common.ETServer;
import dev.kikugie.elytratrims.common.access.FeatureAccess;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;

public class ETGlowRecipe extends DelegatedRecipe {
    public static RecipeSerializer<ETGlowRecipe> SERIALIZER = new SpecialRecipeSerializer<>(ETGlowRecipe::new);
    public ETGlowRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    boolean matches(Inventory inventory) {
        int item = 0;
        int sac = 0;
        for (int slot = 0; slot < inventory.size(); slot++) {
            ItemStack stack = inventory.getStack(slot);
            if (ETServer.isProbablyElytra(stack.getItem())) item++;
            else if (stack.getItem() == Items.GLOW_INK_SAC) sac++;
            else if (!stack.isEmpty()) return false;
            if (item > 1 || sac > 1) return false;
        }
        return item == 1 && sac == 1;
    }

    @Override
    ItemStack craft(Inventory inventory) {
        ItemStack item = ItemStack.EMPTY;
        for (int slot = 0; slot < inventory.size(); slot++) {
            ItemStack stack = inventory.getStack(slot);
            if (!ETServer.isProbablyElytra(stack.getItem())) continue;
            item = stack.copy();
        }
        FeatureAccess.setGlow(item, true);
        return item;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }
}