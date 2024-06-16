package dev.kikugie.elytratrims.mixin.compat;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Restriction(require = {@Condition("betterend")})
@Mixin(targets = {"org.betterx.betterend.item.ArmoredElytra", "org.betterx.betterend.item.CrystaliteElytra"})
public class ElytraItemMixin/*? if <=1.20.4 {*/ implements net.minecraft.item.DyeableItem /*?}*/ {
}