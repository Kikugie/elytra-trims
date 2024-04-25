package dev.kikugie.elytratrims.mixin.common;

import dev.kikugie.elytratrims.common.config.PatternTester;
import dev.kikugie.elytratrims.mixin.plugin.RequireTest;
import net.minecraft.item.ElytraItem;
import org.spongepowered.asm.mixin.Mixin;

@RequireTest(PatternTester.class)
@Mixin(ElytraItem.class)
public class ElytraItemMixin/*? if <=1.20.4 {*/ implements net.minecraft.item.DyeableItem /*?} */ {
}