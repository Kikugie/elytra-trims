package dev.kikugie.elytratrims.mixin.client;

import dev.kikugie.elytratrims.mixin.access.LivingEntityAccessor;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * Used to detect the armor stand inside the smithing table GUI.
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin implements LivingEntityAccessor {
    @Unique
    private boolean isGui = false;

    @Override
    public void elytratrims$markGui() {
        this.isGui = true;
    }

    @Override
    public boolean elytratrims$isGui() {
        return this.isGui;
    }
}
