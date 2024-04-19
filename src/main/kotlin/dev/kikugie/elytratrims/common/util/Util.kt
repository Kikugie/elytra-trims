package dev.kikugie.elytratrims.common.util

import com.mojang.serialization.DataResult
import net.minecraft.item.ElytraItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries

fun <K, V> memoize(provider: (K) -> V): (K) -> V = object : (K) -> V {
    val cache: MutableMap<K, V> = mutableMapOf()

    override fun invoke(key: K): V = cache.computeIfAbsent(key, provider)
}

fun isProbablyElytra(item: Item): Boolean {
    return item is ElytraItem || Registries.ITEM.getId(item).path.contains("elytra")
}

fun <R> DataResult<R>.getAnyway(): R =
    /*? if <=1.20.4 */getOrThrow(false) {}
    /*? if >1.20.4 *//*orThrow*/