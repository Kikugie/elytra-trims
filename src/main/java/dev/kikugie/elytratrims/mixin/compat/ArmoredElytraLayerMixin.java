package dev.kikugie.elytratrims.mixin.compat;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.elytratrims.client.config.RenderType;
import dev.kikugie.elytratrims.client.render.ETRenderer;
import dev.kikugie.elytratrims.mixin.plugin.MixinConfigurable;
import dev.kikugie.elytratrims.mixin.plugin.RequireMod;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.betterx.betterend.client.render.ArmoredElytraLayer;
import org.betterx.betterend.item.model.ArmoredElytraModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@MixinConfigurable
@RequireMod("betterend")
@Mixin(ArmoredElytraLayer.class)
public class ArmoredElytraLayerMixin {
    /*? if fabric {*/
    @ModifyExpressionValue(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isPartVisible(Lnet/minecraft/client/render/entity/PlayerModelPart;)Z"))
    private boolean betterend$cancelCapeRender(boolean original, @Local(argsOnly = true) LivingEntity entity) {
        return ETRenderer.shouldRender(RenderType.CAPE, entity) && original;
    }

    @WrapOperation(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V",
            at = @At(value = "INVOKE",
                    target = "Lorg/betterx/betterend/item/model/ArmoredElytraModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"))
    private void betterend$elytraPostRender(
            ArmoredElytraModel<?> model,
            MatrixStack matrices,
            VertexConsumer vertices,
            int light,
            int overlay,
            float red,
            float green,
            float blue,
            float alpha,
            Operation<Void> original,
            @Local(argsOnly = true) VertexConsumerProvider provider,
            @Local(argsOnly = true) LivingEntity entity,
            @Local ItemStack stack) {
        original.call(model, matrices, vertices, light, overlay, red, green, blue, alpha);
        ETRenderer.render(model, matrices, provider, entity, stack, light, alpha);
    }
    /*?} */
}