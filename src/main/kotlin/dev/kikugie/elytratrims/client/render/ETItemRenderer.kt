package dev.kikugie.elytratrims.client.render

import dev.kikugie.elytratrims.client.CLIENT
import dev.kikugie.elytratrims.client.ETClient
import dev.kikugie.elytratrims.common.util.isProbablyElytra
import dev.kikugie.elytratrims.mixin.client.LivingEntityRendererAccessor
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.ArmorStandEntityRenderer
import net.minecraft.client.render.entity.model.ArmorStandArmorEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.collection.DefaultedList

object ETItemRenderer {
    private var dummy: ArmorStandEntity? = null

    @JvmStatic
    fun render(stack: ItemStack, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int): Boolean {
        if (CLIENT.world == null) return false
        if (!ETClient.config.texture.useElytraModel.value) return false
        if (!isProbablyElytra(stack.item)) return false
        if (dummy == null || dummy?.world != CLIENT.world)
            dummy = ArmorStandEntity(CLIENT.world, 0.0, 0.0, 0.0)
        val renderer = CLIENT.entityRenderDispatcher.getRenderer(dummy) as ArmorStandEntityRenderer
        renderer as LivingEntityRendererAccessor
        matrices.push()
        renderer.model.child = true
        renderer.model.riding = false
        renderer.invokeSetupTransforms(
            dummy!!,
            matrices,
            0F,
            0F,
            CLIENT.tickDelta,
            /*? if >1.20.4 *//*0F */
        )
        val scalar = 1.25F
        matrices.scale(scalar, -scalar, -scalar)
        matrices.translate(-0.4F, -1.45F, 0F)
        val items = dummy!!.armorItems as DefaultedList<ItemStack>
        val slot = EquipmentSlot.CHEST.entitySlotId
        items[slot] = stack
        for (it in renderer.getFeatures<ArmorStandEntity, ArmorStandArmorEntityModel>()) it.render(
            matrices,
            vertexConsumers,
            light,
            dummy!!,
            0F,
            0F,
            CLIENT.tickDelta,
            0F,
            0F,
            0F
        )
        items[slot] = ItemStack.EMPTY
        matrices.pop()
        return true
    }
}