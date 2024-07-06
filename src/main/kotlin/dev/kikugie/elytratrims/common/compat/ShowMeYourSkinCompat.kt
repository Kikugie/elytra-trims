package dev.kikugie.elytratrims.common.compat

import net.minecraft.entity.Entity

object ShowMeYourSkinCompat {
    fun getElytraTransparency(original: Float, entity: Entity?): Float =
        //? if >=1.21 {
        /*runWithMod("showmeyourskin") {
            if (entity == null) original
            else nl.enjarai.showmeyourskin.config.ModConfig.INSTANCE.getApplicablePieceTransparency(
                entity.uuid,
                nl.enjarai.showmeyourskin.config.HideableEquipment.ELYTRA
            )
        } ?: *//*?}*/ original
}