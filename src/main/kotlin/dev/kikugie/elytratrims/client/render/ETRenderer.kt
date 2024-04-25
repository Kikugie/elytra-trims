package dev.kikugie.elytratrims.client.render

import dev.kikugie.elytratrims.client.CLIENT
import dev.kikugie.elytratrims.client.ETClient
import dev.kikugie.elytratrims.client.config.RenderMode.*
import dev.kikugie.elytratrims.client.config.RenderType
import dev.kikugie.elytratrims.client.resource.ETAtlasHolder
import dev.kikugie.elytratrims.common.access.FeatureAccess.hasGlow
import dev.kikugie.elytratrims.common.util.memoize
import dev.kikugie.elytratrims.mixin.access.LivingEntityAccessor
import net.minecraft.client.model.Model
import net.minecraft.client.render.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

object ETRenderer {
    @Suppress("INACCESSIBLE_TYPE")
    @JvmField
    val layer: (Identifier) -> RenderLayer = memoize {
        RenderLayer.of(
            "elytra_layer",
            VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
            VertexFormat.DrawMode.QUADS,
            256,
            true,
            true,
            RenderLayer.MultiPhaseParameters.builder()
                .program(RenderPhase.ENTITY_NO_OUTLINE_PROGRAM)
                .texture(RenderPhase.Texture(it, false, false))
                .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
                .cull(RenderPhase.DISABLE_CULLING)
                .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                .overlay(RenderPhase.ENABLE_OVERLAY_COLOR)
                .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
                .writeMaskState(RenderPhase.COLOR_MASK)
                .build(true)
        )
    }
    private val renderers: MutableList<FeatureRenderer> = mutableListOf<FeatureRenderer>().apply {
        add(ColorOverlayRenderer())
        add(PatternsOverlayRenderer())
        add(TrimOverlayRenderer())
    }

    fun reset() = with(renderers) {
        clear()
        add(ColorOverlayRenderer())
        add(PatternsOverlayRenderer())
        add(TrimOverlayRenderer())
    }

    @JvmStatic
    fun render(
        model: Model,
        matrices: MatrixStack,
        provider: VertexConsumerProvider,
        entity: LivingEntity?,
        stack: ItemStack,
        light: Int,
        alpha: Float,
    ) {
        if (!ETAtlasHolder.ready) return
        val effectiveLight = if (!stack.hasGlow() || !(entity == null || shouldRender(RenderType.GLOW, entity))) light
        else 0xFF00FF
        renderers.forEach {
            it.render(model, matrices, provider, entity, stack, effectiveLight, alpha)
        }
    }

    @JvmStatic
    fun shouldRender(type: RenderType, entity: LivingEntity): Boolean {
        val mode = ETClient.config.render.effective(type)
        val player = CLIENT.player
        return renderAlways(entity) || when (mode) {
            NONE -> false
            SELF -> entity == player
            OTHERS -> entity != player
            ALL -> true
        }
    }

    @JvmStatic
    fun renderAlways(entity: LivingEntity): Boolean = (entity as LivingEntityAccessor).`elytra_trims$isGui`()
}