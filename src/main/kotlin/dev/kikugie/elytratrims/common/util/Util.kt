package dev.kikugie.elytratrims.common.util

import com.mojang.serialization.DataResult
import net.minecraft.item.ArmorItem
import net.minecraft.item.ElytraItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier

fun <K, V> memoize(provider: (K) -> V): (K) -> V = object : (K) -> V {
    val cache: MutableMap<K, V> = mutableMapOf()

    override fun invoke(key: K): V = cache.computeIfAbsent(key, provider)
}

val Item.id get() = Registries.ITEM.getId(this)

fun isProbablyElytra(item: Item): Boolean = when(item) {
    is ElytraItem -> true
    /*? if fabric*/
    is net.fabricmc.fabric.api.entity.event.v1.FabricElytraItem -> true
    else -> item is ArmorItem && item.id.path.contains("elytra")
}

fun <R> DataResult<R>.getAnyway(): R =
    /*? if <=1.20.4*/getOrThrow(false) {}
    /*? if >1.20.4*//*orThrow*/  

fun identifier(id: String): Identifier =
    /*? if <1.21 {*/Identifier(id)
    /*?} else*//*if (':' in id) Identifier.of(id) else Identifier.ofVanilla(id)*/

fun identifier(path: String, id: String): Identifier =
    /*? if <1.21 {*/Identifier(path, id)
    /*?} else*//*Identifier.of(path, id)*/

fun DyeColor.components(alpha: Float = 1F) =
    /*? if <1.21 {*/colorComponents.toARGB().withAlpha(alpha)
    /*?} else*//*entityColor.withAlpha(alpha)*/