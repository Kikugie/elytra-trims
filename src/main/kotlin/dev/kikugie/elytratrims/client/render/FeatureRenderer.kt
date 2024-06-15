package dev.kikugie.elytratrims.client.render

import com.bawnorton.allthetrims.client.util.PaletteHelper
import dev.kikugie.elytratrims.client.CLIENT
import dev.kikugie.elytratrims.client.ETClient
import dev.kikugie.elytratrims.client.config.RenderType
import dev.kikugie.elytratrims.client.resource.ETAtlasHolder
import dev.kikugie.elytratrims.client.resource.missing
import dev.kikugie.elytratrims.common.ETReference
import dev.kikugie.elytratrims.common.access.FeatureAccess.getAnimationStatus
import dev.kikugie.elytratrims.common.access.FeatureAccess.getColor
import dev.kikugie.elytratrims.common.access.FeatureAccess.getPatterns
import dev.kikugie.elytratrims.common.access.FeatureAccess.getTrims
import dev.kikugie.elytratrims.common.util.*
import dev.kikugie.elytratrims.platform.ModStatus
import net.minecraft.block.entity.BannerPattern
import net.minecraft.client.model.Model
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.TexturedRenderLayers
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.trim.ArmorTrim
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper.hsvToRgb

private val missing = mutableSetOf<Identifier>()

private fun report(id: Identifier) {
    if (missing.add(id)) ETReference.LOGGER.warn("Texture $id is missing and will be skipped")
}

interface FeatureRenderer {
    val type: RenderType
    val atlas get() = ETAtlasHolder.atlas
    fun render(
        model: Model,
        matrices: MatrixStack,
        provider: VertexConsumerProvider,
        entity: LivingEntity?,
        stack: ItemStack,
        light: Int,
        color: ARGB
    )

    fun createVertexConsumer(sprite: Sprite, provider: VertexConsumerProvider, stack: ItemStack): VertexConsumer =
        sprite.getTextureSpecificVertexConsumer(
            ItemRenderer.getDirectItemGlintConsumer(
                provider,
                ETRenderer.layer(ETAtlasHolder.id),
                false,
                stack.hasGlint()
            )
        )

    fun Model.render(
        sprite: Sprite,
        matrices: MatrixStack,
        provider: VertexConsumerProvider,
        stack: ItemStack,
        light: Int,
        color: Int
    ): Unit = render(
        matrices,
        createVertexConsumer(sprite, provider, stack),
        light,
        OverlayTexture.DEFAULT_UV,
        //? if <1.21 {
        color.red.scaled,
        color.green.scaled,
        color.blue.scaled,
        color.alpha.scaled,
        //?} else
        /*color*/
    )
}

class ColorOverlayRenderer : FeatureRenderer {
    override val type = RenderType.COLOR
    private val sprite: Sprite by lazy {
        val id = identifier("entity/elytra")
        atlas.getSprite(id).apply { if (missing) report(id) }
    }

    override fun render(
        model: Model,
        matrices: MatrixStack,
        provider: VertexConsumerProvider,
        entity: LivingEntity?,
        stack: ItemStack,
        light: Int,
        color: ARGB
    ) {
        if (sprite.missing) return
        val itemColor = if (stack.name.string == "jeb_")
            hsvToRgb((CLIENT.world?.time ?: 0) % 360 / 360F, 1F, 1F)
        else stack.getColor()
        if (itemColor == 0) return
        model.render(sprite, matrices, provider, stack, light, itemColor.withAlpha(color.alpha))
    }
}

class PatternsOverlayRenderer : FeatureRenderer {
    override val type = RenderType.PATTERNS
    private val cache: (RegistryEntry<BannerPattern>) -> Sprite = memoize {
        val key = it/*? if <=1.20.4*/.key.get()
        val useBanner = ETClient.config.texture.useBannerTextures.value
        val spriteId = if (useBanner)
            TexturedRenderLayers.getBannerPatternTextureId(key).textureId
        else
            TexturedRenderLayers.getShieldPatternTextureId(key).textureId
        atlas.getSprite(spriteId).apply { if (missing) report(spriteId) }
    }

    override fun render(
        model: Model,
        matrices: MatrixStack,
        provider: VertexConsumerProvider,
        entity: LivingEntity?,
        stack: ItemStack,
        light: Int,
        color: ARGB
    ) = stack.getPatterns().forEach {
        val sprite = cache(it.pattern)
        if (sprite.missing) return@forEach
        model.render(sprite, matrices, provider, stack, light, it.color.toArgb(color.alpha))
    }
}

class TrimOverlayRenderer : FeatureRenderer {
    override val type = RenderType.TRIMS
    private val vanillaCache: (ArmorTrim) -> Sprite = memoize {
        val id: Identifier = it.pattern.value().assetId.withPath { path ->
            "trims/models/elytra/${path}_${it.material.value().assetName}"
        }
        sprite(id)
    }
    private val attCache: (TrimInfo) -> Sprite = memoize {
        val material = it.trim.material.value().assetName
        val id = it.trim.pattern.value().assetId.withPath { path ->
            "trims/models/elytra/${path}_${it.index}_$material"
        }
        sprite(id)
    }

    private fun sprite(id: Identifier): Sprite {
        if (ETClient.config.texture.useDarkerTrim.value) {
            val sprite = atlas.getSprite(id.withSuffixedPath("_darker"))
            if (!sprite.missing) return sprite
        }
        return atlas.getSprite(id).apply { if (missing) report(id) }
    }

    override fun render(
        model: Model,
        matrices: MatrixStack,
        provider: VertexConsumerProvider,
        entity: LivingEntity?,
        stack: ItemStack,
        light: Int,
        color: ARGB
    ) = stack.getTrims(
        entity?.world?.registryManager ?: CLIENT.world?.registryManager
        ?: throw AssertionError("No available world - nowhere to get trims from")
    ).forEach {
        val sprite = vanillaCache(it)
        if (!sprite.missing)
            model.render(sprite, matrices, provider, stack, light, color)
        else if (ModStatus.isLoaded("allthetrims"))
            renderTrimExtended(model, matrices, provider, entity, stack, it, light, color)
        else if (entity != null && ETRenderer.renderAlways(entity))
            model.render(sprite, matrices, provider, stack, light, color)
    }

    private fun renderTrimExtended(
        model: Model,
        matrices: MatrixStack,
        provider: VertexConsumerProvider,
        entity: LivingEntity?,
        stack: ItemStack,
        trim: ArmorTrim,
        light: Int,
        color: ARGB
    ) {
        val palette = PaletteHelper.getPalette(trim.material.value().ingredient.value())
        for (i in 0 until 8) {
            val sprite = attCache(TrimInfo(trim, i))
            if (sprite.missing && !(entity == null || ETRenderer.renderAlways(entity))) continue
            model.render(sprite, matrices, provider, stack, light, palette[i].rgb.withAlpha(color.alpha))
        }
    }

    private data class TrimInfo(val trim: ArmorTrim, val index: Int)
}

class AnimationRenderer : FeatureRenderer {
    override val type: RenderType = RenderType.GLOBAL
    private val background: Sprite by lazy {
        val id = identifier("entity/shield/base")
        atlas.getSprite(id).apply { if (missing) report(id) }
    }
    private val animation: Sprite by lazy {
        val id = ETReference.id("animation/animation")
        atlas.getSprite(id).apply { if (missing) report(id) }
    }

    override fun render(
        model: Model,
        matrices: MatrixStack,
        provider: VertexConsumerProvider,
        entity: LivingEntity?,
        stack: ItemStack,
        light: Int,
        color: ARGB
    ) {
        if (!ETClient.config.texture.animationEasterEgg.value || !stack.getAnimationStatus()) return
        model.render(background, matrices, provider, stack, light, color.alpha shl 24)
        model.render(animation, matrices, provider, stack, light, 0xFFFFFF.withAlpha(color.alpha))
    }
}