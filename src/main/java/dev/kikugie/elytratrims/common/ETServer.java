package dev.kikugie.elytratrims.common;

import dev.kikugie.elytratrims.common.access.FeatureAccess;
import dev.kikugie.elytratrims.mixin.access.ElytraOverlaysAccessor;
import dev.kikugie.elytratrims.common.config.ServerConfigs;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;

public class ETServer {
    public static CauldronBehavior CLEAN_ELYTRA;

    public static void init() {
        ETCommentary.run();

        CLEAN_ELYTRA = (state, world, pos, player, hand, stack) -> {
            boolean glowRemoval = false;
            boolean bannerRemoval = false;
            boolean dyeRemoval = false;

            if (FeatureAccess.hasGlow(stack)) {
                FeatureAccess.setGlow(stack, false);
                glowRemoval = true;
            }
            if (FeatureAccess.getColor(stack) != 0) {
                FeatureAccess.setColor(stack, 0);
                dyeRemoval = true;
            }
            if (!((ElytraOverlaysAccessor) (Object) stack).elytra_trims$getPatterns().isEmpty()) {
                NbtCompound nbt = BlockItem.getBlockEntityNbt(stack);
                if (nbt != null) nbt.remove("Patterns");
                bannerRemoval = true;
            }
            if (glowRemoval || bannerRemoval || dyeRemoval) {
                player.incrementStat(Stats.CLEAN_ARMOR);
                LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
                return ActionResult.success(world.isClient);
            }
            return ActionResult.PASS;
        };

        if (ServerConfigs.getConfig().cleanableElytra) CauldronBehavior.WATER_CAULDRON_BEHAVIOR
                /*? if >1.20.2 */
                /*.map()*/
                .put(Items.ELYTRA, CLEAN_ELYTRA::interact);
    }

    public static boolean isProbablyElytra(Item item) {
        return item instanceof ElytraItem || ETServerWrapper.getItemId(item).getPath().contains("elytra");
    }
}