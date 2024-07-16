package dev.kikugie.elytratrims.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.elytratrims.api.ElytraTrimsAPI;
import dev.kikugie.elytratrims.client.config.RenderType;
import dev.kikugie.elytratrims.client.render.ETRenderer;
import dev.kikugie.elytratrims.common.util.ColorKt;
import dev.kikugie.elytratrims.mixin.constants.Targets;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ElytraFeatureRenderer.class, priority = 1100)
public class ElytraFeatureRendererMixin {
    /**
     * Renders player's cape on the armor stand in a smithing table.
     * Injects at isPartVisible because of changes from 1.20.1 to 1.20.2.
     */
    @ModifyExpressionValue(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = Targets.isPartVisible))
    private boolean cancelCapeRender(boolean original, @Local(argsOnly = true) LivingEntity entity) {
        return ETRenderer.shouldRender(RenderType.CAPE, entity) && original;
    }

    /**
     * Handles rendering of the mod features
     */
    @WrapOperation(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = Targets.renderModel))
    private void elytraPostRender(
            ElytraEntityModel<?> model,
            MatrixStack matrices,
            VertexConsumer vertices,
            int light,
            int overlay,
            //? if <1.21 {
            float red,
            float green,
            float blue,
            float alpha,
            //?}
            Operation<Void> operation,
            @Local(ordinal = 0) ItemStack stack,
            @Local(argsOnly = true) VertexConsumerProvider provider,
            @Local(argsOnly = true) LivingEntity entity) {
        //$ render_call {
        operation.call(model, matrices, vertices, light, overlay, red, green, blue, alpha);
        ElytraTrimsAPI.renderFeatures(model, matrices, provider, entity, stack, light, red, green, blue, alpha);//$}
    }
}
