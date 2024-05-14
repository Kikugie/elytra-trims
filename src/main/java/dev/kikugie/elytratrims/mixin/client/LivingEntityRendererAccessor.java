package dev.kikugie.elytratrims.mixin.client;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(LivingEntityRenderer.class)
public interface LivingEntityRendererAccessor {
    @Invoker
    void invokeSetupTransforms(@NotNull LivingEntity dummy, @NotNull MatrixStack matrices, float fl, float fl1, float tickDelta);

    @Accessor
    <T extends LivingEntity, M extends EntityModel<T>> List<FeatureRenderer<T, M>> getFeatures();
}
