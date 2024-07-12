package dev.kikugie.elytratrims.common

import dev.kikugie.elytratrims.common.access.FeatureAccess.getAnimationStatus
import dev.kikugie.elytratrims.common.access.FeatureAccess.getColor
import dev.kikugie.elytratrims.common.access.FeatureAccess.getPatterns
import dev.kikugie.elytratrims.common.access.FeatureAccess.hasGlow
import dev.kikugie.elytratrims.common.access.FeatureAccess.removeAnimationStatus
import dev.kikugie.elytratrims.common.access.FeatureAccess.removeColor
import dev.kikugie.elytratrims.common.access.FeatureAccess.removeGlow
import dev.kikugie.elytratrims.common.access.FeatureAccess.removePatterns
import dev.kikugie.elytratrims.common.config.ETServerConfig
import dev.kikugie.elytratrims.common.util.elytras
import dev.kikugie.elytratrims.platform.ModStatus
import net.minecraft.block.LeveledCauldronBlock
import net.minecraft.block.cauldron.CauldronBehavior
import net.minecraft.stat.Stats

object ETCommon {
    @JvmField
    val config: ETServerConfig = if (ModStatus.isClient) ETServerConfig() else ETServerConfig.load()
    private var initialized = false

    fun init() {
        ETCommentary.run()
    }

    @JvmStatic
    fun postInit() {
        if (initialized) return
        val behaviour = CauldronBehavior { state, world, pos, player, _, stack ->
            var success = false

            if (stack.hasGlow()) {
                stack.removeGlow()
                success = true
            }
            if (stack.getColor() != 0) {
                stack.removeColor()
                success = true
            }
            if (stack.getPatterns().isNotEmpty()) {
                stack.removePatterns()
                success = true
            }
            if (stack.getAnimationStatus()) {
                stack.removeAnimationStatus()
                success = true
            }
            val result = if (success) {
                player.incrementStat(Stats.CLEAN_ARMOR)
                LeveledCauldronBlock.decrementFluidLevel(state, world, pos)
                true
            } else false
            /*? if <=1.20.4 {*/
            return@CauldronBehavior if (result) net.minecraft.util.ActionResult.success(world.isClient)
            else net.minecraft.util.ActionResult.PASS
            /*?} else {*/
            /*return@CauldronBehavior if (result) net.minecraft.util.ItemActionResult.success(world.isClient)
            else net.minecraft.util.ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
              *//*?}*/
        }
        val map = CauldronBehavior.WATER_CAULDRON_BEHAVIOR/*? if >1.20.2*//*.map()*/
        for (elytra in elytras) {
            val existing = map[elytra]
            if (existing == null) map[elytra] = behaviour
            else map[elytra] = CauldronBehavior { state, world, pos, player, hand, stack ->
                val res1 = existing.interact(state, world, pos, player, hand, stack)
                val res2 = behaviour.interact(state, world, pos, player, hand, stack)
                // Returns the most successful result
                if (res1.ordinal <= res2.ordinal) res1 else res2
            }
        }
        initialized = true
    }
}