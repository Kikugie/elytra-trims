package dev.kikugie.elytratrims.client.render

import dev.kikugie.elytratrims.api.ElytraTrimsAPI
import dev.kikugie.elytratrims.client.CLIENT
import dev.kikugie.elytratrims.client.ETClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.collection.DefaultedList

object ETItemRenderer {
    private val delegated: MutableList<Holder> = mutableListOf()
    private var dummy: ArmorStandEntity? = null

    @JvmStatic
    fun shouldRender(stack: ItemStack): Boolean = when {
        CLIENT.world == null -> false
        !ETClient.config.texture.useElytraModel -> false
        !ElytraTrimsAPI.isProbablyElytra(stack) -> false
        else -> true
    }

    @JvmStatic
    fun schedule(stack: ItemStack, matrices: MatrixStack, consumers: VertexConsumerProvider, light: Int) {
        val current = matrices.peek()
        val copy = MatrixStack().apply {
            peek().positionMatrix.set(current.positionMatrix)
            peek().normalMatrix.set(current.normalMatrix)
        }
        delegated += Holder(stack, copy, consumers, light)
    }

    @JvmStatic
    fun execute() {
        if (CLIENT.world != null) delegated.forEach {
            val (stack, matrices, consumers, light) = it
            render(stack, matrices, consumers, light)
        }
        delegated.clear()
    }

    @JvmStatic
    fun render(stack: ItemStack, matrices: MatrixStack, consumers: VertexConsumerProvider, light: Int) {
        if (dummy == null || dummy?.world != CLIENT.world)
            dummy = ArmorStandEntity(CLIENT.world, 0.0, 0.0, 0.0).apply { isInvisible = true }
        matrices.push()
        matrices.scale(0.65F, 0.65F, -1F)
        val items = dummy!!.armorItems as DefaultedList<ItemStack>
        val slot = EquipmentSlot.CHEST.entitySlotId
        items[slot] = stack
        CLIENT.entityRenderDispatcher.render(
            dummy,
            0.775,
            -0.125,
            -0.2,
            0F,
            0F,
            matrices,
            consumers,
            light
        )
        items[slot] = ItemStack.EMPTY
        matrices.pop()
    }

    private data class Holder(
        val stack: ItemStack,
        val matrices: MatrixStack,
        val consumers: VertexConsumerProvider,
        val light: Int
    )
}