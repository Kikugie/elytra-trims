package dev.kikugie.elytratrims.mixin.client;

import dev.kikugie.elytratrims.client.render.ETItemRenderer;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WorldRenderer.class, priority = 1100)
public class WorldRendererMixin {
	@Inject(method = "render", at = @At(value = "CONSTANT", args = "stringValue=blockentities", ordinal = 0))
	private void renderScheduled(CallbackInfo ci) {
		ETItemRenderer.execute();
	}
}
