package dev.kikugie.elytratrims.common.compat

import com.bawnorton.allthetrims.client.util.PaletteHelper
import dev.kikugie.elytratrims.client.render.ETRenderer
import dev.kikugie.elytratrims.client.render.TrimOverlayRenderer.TrimInfo
import dev.kikugie.elytratrims.client.render.render
import dev.kikugie.elytratrims.client.resource.missing
import dev.kikugie.elytratrims.common.util.ARGB
import dev.kikugie.elytratrims.common.util.alpha
import dev.kikugie.elytratrims.common.util.withAlpha
import dev.kikugie.elytratrims.platform.ModStatus
import net.minecraft.client.model.Model
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.trim.ArmorTrim

object AllTheTrimsCompat {
    val isLegacyATT = ModStatus.getVersion("allthetrims")?.startsWith('3') == true

    fun renderTrimExtended(
        model: Model,
        matrices: MatrixStack,
        provider: VertexConsumerProvider,
        entity: LivingEntity?,
        stack: ItemStack,
        trim: ArmorTrim,
        light: Int,
        color: ARGB,
        cache: (TrimInfo) -> Sprite
    ) {
        val palette = PaletteHelper.getPalette(trim.material.value().ingredient.value())
        for (i in 0 until 8) {
            val sprite = cache(TrimInfo(trim, i))
            if (sprite.missing && !(entity == null || ETRenderer.renderAlways(entity))) continue
            model.render(sprite, matrices, provider, stack, light, palette[i].rgb.withAlpha(color.alpha))
        }
    }
}