package dev.kikugie.elytratrims.common.access;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.DyeColor;

public record BannerLayer(RegistryEntry<BannerPattern> pattern, DyeColor color) {
}
