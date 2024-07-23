package dev.kikugie.elytratrims.mixin.compat.render;

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
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.betterx.betterend.client.render.ArmoredElytraLayer;
import org.betterx.betterend.item.model.ArmoredElytraModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Restriction(require = {@Condition("betterend")})
@Mixin(ArmoredElytraLayer.class)
public class BetterEndMixin {
    //? if fabric {
    @ModifyExpressionValue(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = Targets.isPartVisible))
    private boolean betterend$cancelCapeRender(boolean original, @Local(argsOnly = true) LivingEntity entity) {
        return ElytraTrimsAPI.shouldShowCape(entity) && original;
    }

    @WrapOperation(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V",
            at = @At(
                    value = "INVOKE",
                    //? if <1.21 {
                    target = "Lorg/betterx/betterend/item/model/ArmoredElytraModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"
                    //?} else
                    /*target = "Lorg/betterx/betterend/item/model/ArmoredElytraModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V"*/
            )
    )
    private void betterend$elytraPostRender(
            ArmoredElytraModel<?> model,
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
            @Local ItemStack stack,
            @Local(argsOnly = true) VertexConsumerProvider provider,
            @Local(argsOnly = true) LivingEntity entity) {
        //$ render_call {
        operation.call(model, matrices, vertices, light, overlay, red, green, blue, alpha);
        ElytraTrimsAPI.renderFeatures(model, matrices, provider, entity, stack, light, red, green, blue, alpha);//$}
    }
    //?}
}