package dev.kikugie.elytratrims.mixin.compat;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.elytratrims.client.config.RenderType;
import dev.kikugie.elytratrims.client.render.ETRenderer;
import dev.kikugie.elytratrims.common.util.ColorKt;
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
@SuppressWarnings("ALL")
@Restriction(require = {@Condition("minecraftcapes")})
@Mixin(value = ElytraFeatureRenderer.class, priority = 1500)
public class MinecraftCapesCompatMixin {
    @TargetHandler(mixin = "net.minecraftcapes.mixin.MixinElytraLayer", name = "render")
    @ModifyExpressionValue(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = Targets.isPartVisible))
    private boolean minecraftcapes$cancelCapeRender(boolean original, @Local(argsOnly = true) LivingEntity entity) {
        return ETRenderer.shouldRender(RenderType.CAPE, entity) && original;
    }

    @TargetHandler(mixin = "net.minecraftcapes.mixin.MixinElytraLayer", name = "render")
    @ModifyExpressionValue(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/minecraftcapes/config/MinecraftCapesConfig;isCapeVisible()Z"))
    private boolean minecraftcapes$cancelCapeRender2(boolean original, @Local(argsOnly = true) LivingEntity entity) {
        return ETRenderer.shouldRender(RenderType.CAPE, entity) && original;
    }

    // FIXME when minecraft capes updates
    @TargetHandler(mixin = "net.minecraftcapes.mixin.MixinElytraLayer", name = "render")
    @WrapOperation(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/ElytraEntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"))
    private void minecraftcapes$elytraPostRender(ElytraEntityModel<?> model,
                                  MatrixStack matrices,
                                  VertexConsumer vertices,
                                  int light,
                                  int overlay,
                                  float red,
                                  float green,
                                  float blue,
                                  float alpha,
                                  Operation<ElytraEntityModel<?>> original,
                                  @Local(argsOnly = true) VertexConsumerProvider provider,
                                  @Local(argsOnly = true) LivingEntity entity) {
        original.call(model, matrices, vertices, light, overlay, red, green, blue, alpha);
        ETRenderer.render(model, matrices, provider, entity, entity.getEquippedStack(EquipmentSlot.CHEST), light, ColorKt.toARGB(red, green, blue, alpha));
    }
}