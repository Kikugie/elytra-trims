package dev.kikugie.elytratrims.common.compat

import dev.kikugie.elytratrims.client.render.TrimOverlayRenderer.TrimInfo
import dev.kikugie.elytratrims.client.resource.missing
import dev.kikugie.elytratrims.common.util.ARGB
import dev.kikugie.elytratrims.platform.ModStatus
import net.minecraft.client.model.Model
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.trim.ArmorTrim

//? if <1.21 {
import com.bawnorton.allthetrims.client.util.PaletteHelper
import dev.kikugie.elytratrims.client.render.ETRenderer
import dev.kikugie.elytratrims.client.render.render
import dev.kikugie.elytratrims.common.util.alpha
import dev.kikugie.elytratrims.common.util.withAlpha
//?} else {
/*import com.bawnorton.allthetrims.AllTheTrims
import com.bawnorton.allthetrims.client.AllTheTrimsClient
import dev.kikugie.elytratrims.api.ElytraTrimsAPI
import dev.kikugie.elytratrims.client.resource.ETAtlasHolder.atlas
import net.minecraft.client.render.OverlayTexture
import net.minecraft.util.Identifier
*///?}

object AllTheTrimsCompat {
    val isLegacyATT = ModStatus.getVersion("allthetrims")?.startsWith('3') == true

    fun renderTrimAtt(
        sprite: Sprite,
        model: Model,
        matrices: MatrixStack,
        provider: VertexConsumerProvider,
        entity: LivingEntity?,
        stack: ItemStack,
        trim: ArmorTrim,
        light: Int,
        colour: Int,
        func: (TrimInfo) -> Sprite
    ) {
        if (isLegacyATT && sprite.missing)
            renderTrimExtendedLegacy(model, matrices, provider, entity, stack, trim, light, colour, func)
        else if (isSpriteDynamic(sprite))
            renderTrimExtended(sprite, model, matrices, provider, entity, stack, trim, light, colour)
    }

    private fun renderTrimExtendedLegacy(
        model: Model,
        matrices: MatrixStack,
        provider: VertexConsumerProvider,
        entity: LivingEntity?,
        stack: ItemStack,
        trim: ArmorTrim,
        light: Int,
        color: ARGB,
        cache: (TrimInfo) -> Sprite,
    ) {
        //? if <1.21 {
        val palette = PaletteHelper.getPalette(trim.material.value().ingredient.value())
        for (i in 0 until 8) {
            val sprite = cache(TrimInfo(trim, i))
            if (sprite.missing && !(entity == null || ETRenderer.renderAlways(entity))) continue
            model.render(sprite, matrices, provider, stack, light, palette[i].rgb.withAlpha(color.alpha))
        }
        //?}
    }

    @Suppress("UNUSED_PARAMETER")
    private fun renderTrimExtended(
        sprite: Sprite,
        model: Model,
        matrices: MatrixStack,
        provider: VertexConsumerProvider,
        entity: LivingEntity?,
        stack: ItemStack,
        trim: ArmorTrim,
        light: Int,
        colour: Int,
    ) {
        //? if >=1.21 {
        /*AllTheTrimsClient.getShaderManger().setContext(entity, stack.item)

        val renderer = AllTheTrimsClient.getTrimRenderer()
        val modelId = if (AllTheTrimsClient.getConfig().overrideExisting) {
            Identifier.ofVanilla(
                "trims/models/elytra/%s_%s".format(
                    trim.pattern.value().assetId().path,
                    AllTheTrims.DYNAMIC
                )
            )
        } else {
            renderer.getModelId(sprite)
        }
        val renderLayer = if (renderer.useLegacyRenderer(sprite)) {
            ElytraTrimsAPI.getElytraLayer()
        } else {
            AllTheTrimsClient.getTrimRenderLayer(stack.item, trim)
        }

        renderer.renderTrim(
            trim,
            sprite,
            matrices,
            provider,
            light,
            OverlayTexture.DEFAULT_UV,
            colour,
            modelId,
            atlas,
            renderLayer,
            model::render
        )
        *///?}
    }

    @Suppress("UNUSED_PARAMETER")
    private fun isSpriteDynamic(sprite: Sprite): Boolean {
        //? if >=1.21 {
        /*return AllTheTrimsClient.getTrimRenderer().isSpriteDynamic(sprite)
        *///?} else {
        return false;
        //?}
    }
}