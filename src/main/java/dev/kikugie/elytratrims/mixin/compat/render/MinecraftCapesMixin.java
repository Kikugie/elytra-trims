package dev.kikugie.elytratrims.mixin.compat.render;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.elytratrims.api.ElytraTrimsAPI;
import dev.kikugie.elytratrims.mixin.constants.Targets;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@SuppressWarnings("UnresolvedMixinReference")
@Restriction(require = {@Condition("minecraftcapes")})
@Mixin(value = ElytraFeatureRenderer.class, priority = 1500)
public class MinecraftCapesMixin {
	@TargetHandler(mixin = "net.minecraftcapes.mixin.MixinElytraLayer", name = "render")
	@ModifyExpressionValue(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = Targets.isPartVisible))
	private boolean minecraftcapes$cancelCapeRender(boolean original, @Local(argsOnly = true) LivingEntity entity) {
		return ElytraTrimsAPI.shouldShowCape(entity) && original;
	}

	@TargetHandler(mixin = "net.minecraftcapes.mixin.MixinElytraLayer", name = "render")
	@ModifyExpressionValue(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/minecraftcapes/config/MinecraftCapesConfig;isCapeVisible()Z"))
	private boolean minecraftcapes$cancelCapeRender2(boolean original, @Local(argsOnly = true) LivingEntity entity) {
		return ElytraTrimsAPI.shouldShowCape(entity) && original;
	}

	// FIXME when minecraft capes updates
	@TargetHandler(mixin = "net.minecraftcapes.mixin.MixinElytraLayer", name = "render")
	@WrapOperation(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/ElytraEntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"))
	private void minecraftcapes$elytraPostRender(
			ElytraEntityModel<?> model,
			MatrixStack matrices,
			VertexConsumer vertices,
			int light,
			int overlay,
			float red,
			float green,
			float blue,
			float alpha,
			Operation<Void> operation,
			@Local(argsOnly = true) VertexConsumerProvider provider,
			@Local(argsOnly = true) LivingEntity entity) {
		operation.call(model, matrices, vertices, light, overlay, red, green, blue, alpha);
		ElytraTrimsAPI.renderFeatures(model, matrices, provider, entity, entity.getEquippedStack(EquipmentSlot.CHEST), light, red, green, blue, alpha);
	}
}