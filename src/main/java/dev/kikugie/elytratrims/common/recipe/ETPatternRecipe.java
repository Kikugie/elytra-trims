package dev.kikugie.elytratrims.common.recipe;

import dev.kikugie.elytratrims.common.ETServer;
import dev.kikugie.elytratrims.common.access.FeatureAccess;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;

public class ETPatternRecipe extends DelegatedRecipe {
    public static RecipeSerializer<ETPatternRecipe> SERIALIZER = new SpecialRecipeSerializer<>(ETPatternRecipe::new);
    public ETPatternRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    boolean matches(Inventory inventory) {
        int elytra = 0;
        int banner = 0;
        for (int slot = 0; slot < inventory.size(); slot++) {
            ItemStack stack = inventory.getStack(slot);
            if (ETServer.isProbablyElytra(stack.getItem())) elytra++;
            else if (stack.getItem() instanceof BannerItem) {
                if (BannerBlockEntity.getPatternCount(stack) == 0) return false;
                banner++;
            } else if (!stack.isEmpty()) return false;
            if (elytra > 1 || banner > 1) return false;
        }
        return elytra == 1 && banner == 1;
    }

    @Override
    ItemStack craft(Inventory inventory) {
        ItemStack elytra = ItemStack.EMPTY;
        ItemStack banner = ItemStack.EMPTY;
        for (int slot = 0; slot < inventory.size(); slot++) {
            ItemStack stack = inventory.getStack(slot);
            if (stack.isEmpty()) continue;
            if (ETServer.isProbablyElytra(stack.getItem())) elytra = stack.copy();
            else if (stack.getItem() instanceof BannerItem) banner = stack;
        }
        FeatureAccess.setPatterns(elytra, banner);
        return elytra;
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