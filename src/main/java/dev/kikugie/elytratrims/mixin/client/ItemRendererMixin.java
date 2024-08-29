package dev.kikugie.elytratrims.mixin.client;

import dev.kikugie.elytratrims.client.render.ETItemRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemRenderer.class, priority = 900)
public class ItemRendererMixin {
    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/BakedModel;isBuiltin()Z"), cancellable = true)
    private void renderElytraItem(ItemStack stack, ModelTransformationMode mode, boolean unused, MatrixStack matrices, VertexConsumerProvider consumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        if (!ETItemRenderer.shouldRender(stack)) return;
        switch (mode) {
	        case FIRST_PERSON_LEFT_HAND, FIRST_PERSON_RIGHT_HAND, THIRD_PERSON_RIGHT_HAND, THIRD_PERSON_LEFT_HAND ->
		        ETItemRenderer.schedule(stack, matrices, consumers, light);
            default -> ETItemRenderer.render(stack, matrices, consumers, light);
        }
        matrices.pop();
        ci.cancel();
    }
}