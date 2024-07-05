package dev.kikugie.elytratrims.mixin.compat.render;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.kikugie.elytratrims.api.ElytraTrimsAPI;
import dev.kikugie.elytratrims.mixin.constants.Targets;
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
@SuppressWarnings({"UnresolvedMixinReference", "rawtypes"})
@Restriction(require = {@Condition("elytraslot")})
@Mixin(targets = "com.illusivesoulworks.elytraslot.client.ElytraSlotLayer")
public abstract class ElytraSlotMixin extends FeatureRenderer {
    public ElytraSlotMixin(FeatureRendererContext context) {
        super(context);
    }

    @ModifyExpressionValue(method = "lambda$render$0", at = @At(value = "INVOKE", target = Targets.isPartVisible))
    private boolean elytraslot$cancelCapeRender(boolean original, @Local(argsOnly = true) LivingEntity entity) {
        return ElytraTrimsAPI.shouldShowCape(entity) && original;
    }

    @ModifyExpressionValue(method = "lambda$render$0",
            at = @At(value = "INVOKE",
                    target = "Lcom/illusivesoulworks/elytraslot/client/ElytraRenderResult;stack()Lnet/minecraft/item/ItemStack;"))
    private ItemStack elytraslot$saveItemStack(ItemStack stack, @Share("stack") LocalRef<ItemStack> stackRef) {
        stackRef.set(stack);
        return stack;
    }

    // FIXME when elytra slot updates
    @WrapOperation(method = "lambda$render$0",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/model/ElytraEntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"))
    private void elytraslot$elytraPostRender(
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
            @Share("stack") LocalRef<ItemStack> stack) {
        operation.call(model, matrices, vertices, light, overlay, red, green, blue, alpha);
        ElytraTrimsAPI.renderFeatures(model, matrices, provider, entity, stack.get(), light, red, green, blue, alpha);
    }
}