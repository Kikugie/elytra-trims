package dev.kikugie.elytratrims.mixin.compat;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.kikugie.elytratrims.common.ETReference;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Restriction(require = {@Condition("fabric-registry-sync-v0")})
@Mixin(targets = "net.fabricmc.fabric.impl.registry.sync.RegistrySyncManager", remap = false)
public class FabricAPICompat {
    @WrapWithCondition(method = "createAndPopulateRegistryMap", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2IntMap;put(Ljava/lang/Object;I)I"))
    private static boolean skipElytraRecipes(Object2IntMap<Object> instance, Object object, int value) {
        return !(object instanceof Identifier id && id.getNamespace().equals(ETReference.MOD_ID));
    }
}