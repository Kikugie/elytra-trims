package dev.kikugie.elytratrims.common.access

import net.minecraft.block.entity.BannerPattern
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.DyeColor

data class BannerLayer(val pattern: RegistryEntry<BannerPattern>, val color: DyeColor)
