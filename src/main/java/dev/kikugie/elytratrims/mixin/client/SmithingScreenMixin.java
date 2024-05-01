package dev.kikugie.elytratrims.mixin.client;

import dev.kikugie.elytratrims.mixin.access.ElytraRotationAccessor;
import dev.kikugie.elytratrims.mixin.access.LivingEntityAccessor;
import dev.kikugie.elytratrims.mixin.plugin.MixinConfigurable;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.kikugie.elytratrims.common.util.UtilKt.isProbablyElytra;

@MixinConfigurable
@Mixin(value = SmithingScreen.class, priority = 1100)
public class SmithingScreenMixin implements ElytraRotationAccessor {
    @Unique
    private final Quaternionf dummy = new Quaternionf();
    @Unique
    protected boolean isElytra;
    @Shadow
    @Nullable
    private ArmorStandEntity armorStand;

    @Inject(method = "setup", at = @At("TAIL"))
    private void markGuiArmorStand(CallbackInfo ci) {
        if (armorStand != null) ((LivingEntityAccessor) this.armorStand).elytratrims$markGui();
    }

    @Inject(method = "equipArmorStand", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"), cancellable = true)
    private void equipElytra(ItemStack stack, CallbackInfo ci) {
        if (armorStand == null) return;
        if (isProbablyElytra(stack.getItem())) {
            isElytra = true;
            armorStand.equipStack(EquipmentSlot.CHEST, stack.copy());
            ci.cancel();
        } else isElytra = false;
    }

    @ModifyArg(method = "drawBackground", at = @At(value = "INVOKE",
            /*? if >1.20.4 {*//*
            target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawEntity(Lnet/minecraft/client/gui/DrawContext;FFFLorg/joml/Vector3f;Lorg/joml/Quaternionf;Lorg/joml/Quaternionf;Lnet/minecraft/entity/LivingEntity;)V"
            *//*?} elif >=1.20.2 {*//*
            target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawEntity(Lnet/minecraft/client/gui/DrawContext;FFILorg/joml/Vector3f;Lorg/joml/Quaternionf;Lorg/joml/Quaternionf;Lnet/minecraft/entity/LivingEntity;)V"
            *//*?} elif >=1.20.1 {*/
            target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawEntity(Lnet/minecraft/client/gui/DrawContext;IIILorg/joml/Quaternionf;Lorg/joml/Quaternionf;Lnet/minecraft/entity/LivingEntity;)V"
            /*?} else {*//*
            target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawEntity(Lnet/minecraft/client/util/math/MatrixStack;IIILorg/joml/Quaternionf;Lorg/joml/Quaternionf;Lnet/minecraft/entity/LivingEntity;)V"
            *//*?} */
    ), index = /*? if >=1.20.2 {*//* 5 *//*?} else {*/ 4 /*?} */)
    private Quaternionf applyRotation(Quaternionf quaternionf) {
        return elytratrims$rotateElytra(quaternionf);
    }

    @Override
    public Quaternionf elytratrims$getVector() {
        return dummy;
    }

    @Override
    public boolean elytratrims$isElytra() {
        return isElytra;
    }

    @Override
    public void elytratrims$setElytra(boolean value) {
        isElytra = value;
    }
}