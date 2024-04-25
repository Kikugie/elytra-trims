package dev.kikugie.elytratrims.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import dev.kikugie.elytratrims.client.ETClient;
import dev.kikugie.elytratrims.client.render.ETItemRenderer;
import dev.kikugie.elytratrims.common.util.UtilKt;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/BakedModel;isBuiltin()Z"))
    private void renderElytraItem(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci, @Share("rendered") LocalBooleanRef ref) {
        if (!ETClient.INSTANCE.getConfig().getTexture().getUseElytraModel().getValue() || !UtilKt.isProbablyElytra(stack.getItem()))
            return;
        ETItemRenderer.render(stack, matrices, vertexConsumers, light);
        ref.set(true);
    }

    @ModifyExpressionValue(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/BakedModel;isBuiltin()Z"))
    private boolean cancelBakedItemRenderer(boolean original, @Share("rendered") LocalBooleanRef ref) {
        return ref.get() || original;
    }

    @WrapWithCondition(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/BuiltinModelItemRenderer;render(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V"))
    private boolean cancelBuiltinItemRenderer(BuiltinModelItemRenderer instance, ItemStack item, ModelTransformationMode block, MatrixStack gameProfile, VertexConsumerProvider nbtCompound, int skullType, int skullBlockEntityModel, @Share("rendered") LocalBooleanRef ref) {
        return ref.get();
    }
}