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
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Debug(export = true)
@Pseudo
@SuppressWarnings({"UnresolvedMixinReference", "rawtypes", "InvalidInjectorMethodSignature", "MixinAnnotationTarget"})
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

    @WrapOperation(method = "lambda$render$0", at = @At(value = "INVOKE", target = Targets.renderModelWithColor))
    private void elytraslot$elytraPostRender(
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
            //?} else
            /*int color,*/
            Operation<Void> operation,
            @Local(argsOnly = true) VertexConsumerProvider provider,
            @Local(argsOnly = true) LivingEntity entity,
            @Share("stack") LocalRef<ItemStack> stackRef) {
        ItemStack stack = stackRef.get();
        //$ render_call_color {
        operation.call(model, matrices, vertices, light, overlay, red, green, blue, alpha);
        ElytraTrimsAPI.renderFeatures(model, matrices, provider, entity, stack, light, red, green, blue, alpha);//$}
    }
}