package dev.kikugie.elytratrims.common.recipe

import com.google.gson.JsonObject
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.CraftingRecipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.ShapelessRecipe
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList
import net.minecraft.world.World

abstract class DelegatedRecipe(
    @JvmField val id: Identifier,
    @JvmField val output: ItemStack,
) : CraftingRecipe {
    @JvmField val replacement = ShapelessRecipe(/*? if <1.21 >>*/id, "impossible", category, output, DefaultedList.of())

    //? if >=1.21 {
    /*override fun getResult(lookup: net.minecraft.registry.RegistryWrapper.WrapperLookup?) = output
    *///?} else {
    override fun getId() = id
    override fun getOutput(registryManager: net.minecraft.registry.DynamicRegistryManager?) = output
    //?}
    override fun getCategory(): CraftingRecipeCategory = CraftingRecipeCategory.EQUIPMENT

    abstract fun matches(input: Stacks): Boolean
    abstract fun craft(input: Stacks): ItemStack

    //? if >=1.21 {
    /*override fun craft(input: net.minecraft.recipe.input.CraftingRecipeInput, lookup: net.minecraft.registry.RegistryWrapper.WrapperLookup): ItemStack =
        craft(input.stacks)
    override fun matches(input: net.minecraft.recipe.input.CraftingRecipeInput, world: World): Boolean =
        matches(input.stacks)
    *///?} else {
    override fun craft(inventory: net.minecraft.inventory.RecipeInputInventory, lookup: net.minecraft.registry.DynamicRegistryManager): ItemStack =
        craft(inventory.inputStacks)
    override fun matches(inventory: net.minecraft.inventory.RecipeInputInventory, world: World): Boolean =
        matches(inventory.inputStacks)
    //?}

    class Serializer<T>(private val default: ItemStack, private val init: (Identifier, ItemStack) -> T) : RecipeSerializer<T> where T : DelegatedRecipe {
        //? if >=1.21 {
        /*override fun packetCodec(): net.minecraft.network.codec.PacketCodec<net.minecraft.network.RegistryByteBuf, T>? = null
        override fun codec(): com.mojang.serialization.MapCodec<T> = Identifier.CODEC.xmap(
            { init(it, default) },
            { it.id }
        ).fieldOf("type")
        *///?} else {
        override fun read(id: Identifier, json: JsonObject): T {
            return init(id, default)
        }

        override fun read(id: Identifier, buf: PacketByteBuf): T {
            return init(id, default)
        }

        override fun write(buf: PacketByteBuf, recipe: T) {
            buf.writeItemStack(recipe.output)
        }
        //?}
    }
}