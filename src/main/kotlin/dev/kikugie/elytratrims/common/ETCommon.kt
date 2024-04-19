package dev.kikugie.elytratrims.common

import dev.kikugie.elytratrims.common.access.FeatureAccess.getColor
import dev.kikugie.elytratrims.common.access.FeatureAccess.getPatterns
import dev.kikugie.elytratrims.common.access.FeatureAccess.hasGlow
import dev.kikugie.elytratrims.common.access.FeatureAccess.removeColor
import dev.kikugie.elytratrims.common.access.FeatureAccess.removeGlow
import dev.kikugie.elytratrims.common.access.FeatureAccess.removePatterns
import dev.kikugie.elytratrims.common.config.ETServerConfig
import dev.kikugie.elytratrims.platform.ModStatus
import net.minecraft.block.LeveledCauldronBlock
import net.minecraft.block.cauldron.CauldronBehavior
import net.minecraft.item.Items
import net.minecraft.stat.Stats

object ETCommon {
    lateinit var config: ETServerConfig
        private set

    fun preInit() {
        config = if (ModStatus.isClient) ETServerConfig.default() else ETServerConfig.load()
    }

    fun init() {
        ETCommentary.run()
        if (!config.cleanableElytra) return
        val behaviour = CauldronBehavior { state, world, pos, player, _, stack ->
            var glowRemoval = false
            var bannerRemoval = false
            var dyeRemoval = false

            if (stack.hasGlow()) {
                stack.removeGlow()
                glowRemoval = true
            }
            if (stack.getColor() != 0) {
                stack.removeColor()
                dyeRemoval = true
            }
            if (stack.getPatterns().isNotEmpty()) {
                stack.removePatterns()
                bannerRemoval = true
            }
            val result = if (glowRemoval || bannerRemoval || dyeRemoval) {
                player.incrementStat(Stats.CLEAN_ARMOR)
                LeveledCauldronBlock.decrementFluidLevel(state, world, pos)
                true
            } else false
            /*? if <1.20.4 {*//*
            return@CauldronBehavior if (result) net.minecraft.util.ActionResult.success(world.isClient)
            else net.minecraft.util.ActionResult.PASS
            *//*?} else {*/
            return@CauldronBehavior if (result) net.minecraft.util.ItemActionResult.success(world.isClient)
            else net.minecraft.util.ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
            /*?} */
        }
        CauldronBehavior.WATER_CAULDRON_BEHAVIOR/*? if >1.20.2 */.map()
            .put(Items.ELYTRA, behaviour)
    }
}