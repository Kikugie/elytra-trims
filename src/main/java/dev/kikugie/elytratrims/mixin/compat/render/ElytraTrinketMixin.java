package dev.kikugie.elytratrims.mixin.compat.render;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.elytratrims.api.ElytraTrimsAPI;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@SuppressWarnings({"UnresolvedMixinReference", "rawtypes", "LocalMayBeArgsOnly"})
@Restriction(require = {@Condition("elytra_trinket")})
@Mixin(targets = "pw.lakuna.elytra_trinket.ElytraTrinketFeatureRenderer")
public abstract class ElytraTrinketMixin extends FeatureRenderer {
    public ElytraTrinketMixin(FeatureRendererContext context) {
        super(context);
    }

    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isPartVisible(Lnet/minecraft/client/render/entity/PlayerModelPart;)Z"))
    private boolean elytra_trinket$cancelCapeRender(boolean original, @Local(argsOnly = true) LivingEntity entity) {
        return ElytraTrimsAPI.shouldShowCape(entity) && original;
    }

    // FIXME when elytra trinket updates
    @WrapOperation(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/model/ElytraEntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"))
    private void elytra_trinket$elytraPostRender(
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
            @Local(argsOnly = true) LivingEntity entity,
            @Local ItemStack stack) {
        operation.call(model, matrices, vertices, light, overlay, red, green, blue, alpha);
        ElytraTrimsAPI.renderFeatures(model, matrices, provider, entity, stack, light, red, green, blue, alpha);
    }
}