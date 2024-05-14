package dev.kikugie.elytratrims.client.render

import dev.kikugie.elytratrims.client.CLIENT
import dev.kikugie.elytratrims.client.ETClient
import dev.kikugie.elytratrims.common.util.isProbablyElytra
import dev.kikugie.elytratrims.platform.ModStatus
import dev.tr7zw.firstperson.FirstPersonModelCore
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.collection.DefaultedList

object ETItemRenderer {
    private var dummy: ArmorStandEntity? = null

    @JvmStatic
    fun render(stack: ItemStack, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int): Boolean {
        if (dummy == null || dummy?.world != CLIENT.world) dummy = ArmorStandEntity(CLIENT.world, 0.0, 0.0, 0.0).apply {
            isInvisible = true
        }
        if (!isProbablyElytra(stack.item)) return false
        if (!ETClient.config.texture.useElytraModel.value) return false
        matrices.push()
        matrices.scale(0.75F, 0.75F, -1F)
        val items = dummy!!.armorItems as DefaultedList<ItemStack>
        val slot = EquipmentSlot.CHEST.entitySlotId
        items[slot] = stack
        runWithFirstPersonMod {
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
        }
        items[slot] = ItemStack.EMPTY
        matrices.pop()
        return true
    }

    private inline fun runWithFirstPersonMod(action: () -> Unit) {
        if (!ModStatus.isLoaded("firstperson")) action()
        else {
            val instance = FirstPersonModelCore.instance
            val rendering = instance.isRenderingPlayer
            action()
            instance.isRenderingPlayer = rendering
        }
    }
}