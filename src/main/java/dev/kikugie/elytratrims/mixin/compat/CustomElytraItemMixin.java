package dev.kikugie.elytratrims.mixin.compat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(targets = "net.fabricmc.fabric.api.entity.event.v1.FabricElytraItem")
public interface CustomElytraItemMixin /*? if <=1.20.4 {*/ extends net.minecraft.item.DyeableItem /*?}*/ {
}