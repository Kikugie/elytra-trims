package dev.kikugie.elytratrims.mixin.compat;

import dev.kikugie.elytratrims.common.config.ConfigTesters;
import dev.kikugie.elytratrims.common.plugin.MixinConfigurable;
import dev.kikugie.elytratrims.common.plugin.RequireMod;
import dev.kikugie.elytratrims.common.plugin.RequireTest;
import net.minecraft.item.DyeableItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@RequireTest(ConfigTesters.Patterns.class)
@RequireMod("betterend")
@Mixin(targets = {"org.betterx.betterend.item.ArmoredElytra", "org.betterx.betterend.item.CrystaliteElytra"})
@MixinConfigurable
public class BannerElytraItemMixin
        /*? if <=1.20.4 {*/ implements DyeableItem /*?} */ {
}