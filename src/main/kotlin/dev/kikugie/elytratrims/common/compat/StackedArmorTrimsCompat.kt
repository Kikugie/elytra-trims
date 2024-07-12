package dev.kikugie.elytratrims.common.compat

import net.minecraft.item.ItemStack
import net.minecraft.item.trim.ArmorTrim
import net.minecraft.registry.DynamicRegistryManager

object StackedArmorTrimsCompat {
    fun getTrimList(manager: DynamicRegistryManager, stack: ItemStack): List<ArmorTrim> = /*? if fabric {*/ runWithMod("stacked-armor-trims") {
        io.github.apfelrauber.stacked_trims.ArmorTrimList.getTrims(manager, stack).orElse(emptyList())
    } ?: /*?} */ getVanillaTrim(manager, stack)

    private fun getVanillaTrim(manager: DynamicRegistryManager, stack: ItemStack) = buildList<ArmorTrim> {
        //? if <1.20.2 {
        ArmorTrim.getTrim(manager, stack).ifPresent(::add)
        //?} elif <=1.20.4 >=1.20.2 {
        /*ArmorTrim.getTrim(manager, stack, true).ifPresent(::add)
        *///?} else
        /*stack.get(net.minecraft.component.DataComponentTypes.TRIM)?.let(::add)*/
    }
}