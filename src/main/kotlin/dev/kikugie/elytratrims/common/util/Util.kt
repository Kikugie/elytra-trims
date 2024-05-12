package dev.kikugie.elytratrims.common.util

import com.mojang.serialization.DataResult
import dev.kikugie.elytratrims.common.ETCommon
import dev.kikugie.elytratrims.common.recipe.DelegatedRecipe
import net.minecraft.item.Item
import net.minecraft.recipe.Recipe
import net.minecraft.registry.Registries

fun <K, V> memoize(provider: (K) -> V): (K) -> V = object : (K) -> V {
    val cache: MutableMap<K, V> = mutableMapOf()

    override fun invoke(key: K): V = cache.computeIfAbsent(key, provider)
}

val Item.id get() = Registries.ITEM.getId(this)

fun isProbablyElytra(item: Item): Boolean = item in ETCommon.elytras

fun <R> DataResult<R>.getAnyway(): R =
    /*? if <=1.20.4 */getOrThrow(false) {}
    /*? if >1.20.4 *//*orThrow*/

typealias RecipeWrapper =
    /*? if <1.20.2 */Recipe<*>
    /*? if >=1.20.2 *//*net.minecraft.recipe.RecipeEntry<out Recipe<*>>*/

fun filterRecipes(recipes: Iterable<RecipeWrapper>): Collection<RecipeWrapper> =
    if (ETCommon.config.requireClientSide) recipes.toList() else recipes.filter {
        it/*? if >=1.20.2 {*//*.value*//*?} */ !is DelegatedRecipe
    }