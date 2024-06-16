package dev.kikugie.elytratrims.mixin.common;

import net.minecraft.item.ElytraItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ElytraItem.class)
public class ElytraItemMixin/*? if <=1.20.4 {*/ implements net.minecraft.item.DyeableItem /*?}*/ {
}