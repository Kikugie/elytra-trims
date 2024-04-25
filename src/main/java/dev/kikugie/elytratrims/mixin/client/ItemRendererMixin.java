package dev.kikugie.elytratrims.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import dev.kikugie.elytratrims.client.ETClient;
import dev.kikugie.elytratrims.client.render.ETItemRenderer;
import dev.kikugie.elytratrims.common.util.UtilKt;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ItemRenderer.class, priority = 1500)
public class ItemRendererMixin {
    @WrapWithCondition(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderBakedItemModel(Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;)V"))
    private boolean renderElytraItem(ItemRenderer instance, BakedModel model, ItemStack stack, int light, int overlay, MatrixStack matrices, VertexConsumer consumer, @Share("rendered") LocalBooleanRef ref, @Local(argsOnly = true)VertexConsumerProvider provider) {
        if (!ETClient.INSTANCE.getConfig().getTexture().getUseElytraModel().getValue() || !UtilKt.isProbablyElytra(stack.getItem()))
            return true;
        ETItemRenderer.render(stack, matrices, provider, light);
        return false;
    }
}