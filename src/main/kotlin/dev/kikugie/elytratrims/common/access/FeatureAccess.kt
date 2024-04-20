package dev.kikugie.elytratrims.common.access

import dev.kikugie.elytratrims.common.util.toARGB
import dev.kikugie.elytratrims.platform.ModStatus
import io.github.apfelrauber.stacked_trims.ArmorTrimList
import net.minecraft.item.ItemStack
import net.minecraft.item.trim.ArmorTrim
import net.minecraft.registry.DynamicRegistryManager

/*? if <=1.20.4 {*/
import net.minecraft.block.entity.BannerBlockEntity
import net.minecraft.item.BannerItem
import net.minecraft.item.BlockItem
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.DyeColor

object FeatureAccess : IFeatureAccess {
    private val DYEABLE = object : net.minecraft.item.DyeableItem {}
    override fun ItemStack.getTrims(manager: DynamicRegistryManager): List<ArmorTrim> =
        getArmorTrimList(this, manager) ?:
        getTrim(manager).orElse(null)?.let(::listOf) ?:
        emptyList()

    override fun ItemStack.getPatterns(): List<BannerLayer> {
        val nbt = BannerBlockEntity.getPatternListNbt(this) ?: return emptyList()
        return BannerBlockEntity.getPatternsFromNbt(DyeColor.WHITE, nbt).drop(1).map {
            BannerLayer(it.first, it.second)
        }
    }

    override fun ItemStack.getBaseColor(): Int = (item as? BannerItem)?.color?.fireworkColor ?: 0

    override fun ItemStack.setPatterns(source: ItemStack) {
        val nbt = BannerBlockEntity.getPatternListNbt(source) ?: return
        val container = BlockItem.getBlockEntityNbt(this) ?: NbtCompound()
        if (container.isEmpty) setSubNbt("BlockEntityTag", container)
        container.put("Patterns", nbt.copy())
    }

    override fun ItemStack.removePatterns() {
        val container = BlockItem.getBlockEntityNbt(this) ?: return
        container.remove("Patterns")
        if (container.isEmpty) removeSubNbt("BlockEntityTag")
    }

    override fun ItemStack.getColor() = (item as? BannerItem)?.color?.colorComponents?.toARGB() ?: 0

    override fun ItemStack.setColor(color: Int) {
        DYEABLE.setColor(this, color)
    }

    override fun ItemStack.removeColor() {
        DYEABLE.removeColor(this)
    }

    override fun ItemStack.hasGlow() = getSubNbt("display")?.getBoolean("glow") ?: false

    override fun ItemStack.addGlow() {
        getOrCreateSubNbt("display").putBoolean("glow", true)
    }

    override fun ItemStack.removeGlow() {
        val nbt = getSubNbt("display") ?: return
        nbt.remove("glow")
        if (nbt.isEmpty) removeSubNbt("display")
    }
}
/*?} else {*//*
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.DyedColorComponent
import net.minecraft.component.type.NbtComponent
import net.minecraft.item.BannerItem

object FeatureAccess : IFeatureAccess {
    override fun ItemStack.getTrims(manager: DynamicRegistryManager): List<ArmorTrim> =
        if (ModStatus.isLoaded("stacked-armor-trims")) ArmorTrimList.getTrims(manager, this).orElse(emptyList())
        else get(DataComponentTypes.TRIM)?.let(::listOf) ?: emptyList()

    override fun ItemStack.getPatterns() = get(DataComponentTypes.BANNER_PATTERNS)?.layers?.map {
        BannerLayer(it.pattern, it.color)
    } ?: emptyList()

    override fun ItemStack.getBaseColor(): Int = (item as? BannerItem)?.color?.colorComponents?.toARGB() ?: 0

    override fun ItemStack.setPatterns(source: ItemStack) {
        applyComponentsFrom(source.components.filtered { it == DataComponentTypes.BANNER_PATTERNS })
    }

    override fun ItemStack.removePatterns() {
        remove(DataComponentTypes.BANNER_PATTERNS)
    }

    override fun ItemStack.getColor(): Int = DyedColorComponent.getColor(this, 0)

    override fun ItemStack.setColor(color: Int) {
        set(DataComponentTypes.DYED_COLOR, DyedColorComponent(color, true))
    }

    override fun ItemStack.removeColor() {
        remove(DataComponentTypes.DYED_COLOR)
    }

    // Why copy nbt if we don't modify it
    override fun ItemStack.hasGlow(): Boolean = get(DataComponentTypes.CUSTOM_DATA)?.nbt?.getBoolean("glow") ?: false

    override fun ItemStack.addGlow() {
        val data = (get(DataComponentTypes.CUSTOM_DATA) ?: NbtComponent.DEFAULT).copyNbt()
        data.putBoolean("glow", true)
        NbtComponent.set(DataComponentTypes.CUSTOM_DATA, this, data)
    }

    override fun ItemStack.removeGlow() {
        val data = get(DataComponentTypes.CUSTOM_DATA)?.copyNbt() ?: return
        data.remove("glow")
        NbtComponent.set(DataComponentTypes.CUSTOM_DATA, this, data)
    }
}
*//*?} */

private fun getArmorTrimList(stack: ItemStack, manager: DynamicRegistryManager): List<ArmorTrim>? =
    /*? if fabric {*/
    if (ModStatus.isLoaded("stacked-armor-trims")) ArmorTrimList.getTrims(manager, stack).orElse(null) else null
    /*?} else {*//*
    null
    *//*?} */

/*? if <=1.20.4 >=1.20.2 */
/*private fun ItemStack.getTrim(manager: DynamicRegistryManager) = ArmorTrim.getTrim(manager, this, true)*/
/*? if <1.20.2 */
private fun ItemStack.getTrim(manager: DynamicRegistryManager) = ArmorTrim.getTrim(manager, this)

private interface IFeatureAccess {
    fun ItemStack.getTrims(manager: DynamicRegistryManager): List<ArmorTrim>

    fun ItemStack.getPatterns(): List<BannerLayer>
    fun ItemStack.getBaseColor(): Int
    fun ItemStack.setPatterns(source: ItemStack)
    fun ItemStack.removePatterns()

    fun ItemStack.getColor(): Int
    fun ItemStack.setColor(color: Int)
    fun ItemStack.removeColor()

    fun ItemStack.hasGlow(): Boolean
    fun ItemStack.addGlow()
    fun ItemStack.removeGlow()
}