package dev.kikugie.elytratrims.mixin.compat;

import dev.kikugie.elytratrims.common.config.PatternTester;
import dev.kikugie.elytratrims.mixin.plugin.MixinConfigurable;
import dev.kikugie.elytratrims.mixin.plugin.RequireMod;
import dev.kikugie.elytratrims.mixin.plugin.RequireTest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@RequireTest(PatternTester.class)
@RequireMod("betterend")
@Mixin(targets = {"org.betterx.betterend.item.ArmoredElytra", "org.betterx.betterend.item.CrystaliteElytra"})
@MixinConfigurable
public class ElytraItemMixin/*? if <=1.20.4 {*/ implements net.minecraft.item.DyeableItem /*?}*/ {
}