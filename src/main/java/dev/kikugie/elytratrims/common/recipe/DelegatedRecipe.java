package dev.kikugie.elytratrims.common.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public abstract class DelegatedRecipe extends SpecialCraftingRecipe {
    public DelegatedRecipe(Identifier id, CraftingRecipeCategory category) {
        /*? if <=1.20.4*/
        super(id, category);
        /*? if >1.20.4*/
        /*super(category);*/
    }

    abstract boolean matches(Inventory inventory);
    abstract ItemStack craft(Inventory inventory);

    /*? <=1.20.4 {*/
    @Override
    public boolean matches(net.minecraft.inventory.CraftingInventory inventory, World world) {
        return matches(inventory);
    }

    @Override
    public ItemStack craft(net.minecraft.inventory.CraftingInventory inventory, net.minecraft.registry.DynamicRegistryManager registryManager) {
        return craft(inventory);
    }
    /*?} else {*//*
    @Override
    public boolean matches(net.minecraft.inventory.RecipeInputInventory inventory, World world) {
        return matches(inventory);
    }

    @Override
    public ItemStack craft(net.minecraft.inventory.RecipeInputInventory inventory, net.minecraft.registry.RegistryWrapper.WrapperLookup lookup) {
        return craft(inventory);
    }
    *//*?} */
}