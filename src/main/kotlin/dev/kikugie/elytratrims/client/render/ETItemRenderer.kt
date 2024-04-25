package dev.kikugie.elytratrims.client.render

import dev.kikugie.elytratrims.client.CLIENT
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.collection.DefaultedList

object ETItemRenderer {
    // Armor stand is used because *some* other mods like to implement their own elytra renderers for no good reason.
    private var dummy: ArmorStandEntity? = null

    @JvmStatic
    fun render(stack: ItemStack, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int) {
        if (dummy == null || dummy?.world != CLIENT.world) dummy = ArmorStandEntity(CLIENT.world, 0.0, 0.0, 0.0).apply {
            isInvisible = true
        }
        matrices.push()
        matrices.scale(0.75F, 0.75F, -1F)
        val items = dummy!!.armorItems as DefaultedList<ItemStack>
        val slot = EquipmentSlot.CHEST.entitySlotId
        items[slot] = stack
        CLIENT.entityRenderDispatcher.render(
            dummy,
            0.65,
            -0.1,
            -0.25,
            0F,
            CLIENT.tickDelta,
            matrices,
            vertexConsumers,
            light
        )
        items[slot] = ItemStack.EMPTY
        matrices.pop()
    }
}