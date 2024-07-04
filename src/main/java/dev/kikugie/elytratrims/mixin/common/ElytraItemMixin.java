package dev.kikugie.elytratrims.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(targets = {
		"net.minecraft.item.ElytraItem",
		//? if fabric
		"net.fabricmc.fabric.api.entity.event.v1.FabricElytraItem",
})
public class ElytraItemMixin/*? if <=1.20.4 {*/ implements net.minecraft.item.DyeableItem /*?}*/ {
}