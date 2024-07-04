package dev.kikugie.elytratrims.mixin.compat;

import com.bawnorton.mixinsquared.TargetHandler;
import dev.kikugie.elytratrims.mixin.access.ElytraRotationAccessor;
import dev.kikugie.elytratrims.mixin.constants.Targets;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@SuppressWarnings("UnresolvedMixinReference")
@Restriction(require = {@Condition("bettersmithingtable")})
@Mixin(value = SmithingScreen.class, priority = 1500)
public abstract class BetterSmithingTableMixin implements ElytraRotationAccessor {
    @TargetHandler(
            mixin = "me.bettersmithingtable.mixin.SmithingScreenMixin",
            //? if >=1.20.2 {
            /*name = "renderBg"
            *///?} else
            name = "drawArmorStandPreview"
    )
    @ModifyArg(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = Targets.drawEntity), index = Targets.drawEntityIndex)
    private Quaternionf applyElytraRotation(Quaternionf quaternionf) {
        return elytratrims$rotateElytra(quaternionf);
    }
}