package dev.kikugie.elytratrims.api;

import dev.kikugie.elytratrims.client.config.RenderType;
import dev.kikugie.elytratrims.client.render.ETRenderer;
import dev.kikugie.elytratrims.common.util.ColorKt;
import dev.kikugie.elytratrims.common.util.UtilKt;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElytraTrimsAPI {
	/**
	 * Provides feature rendering access for custom elytras.
	 *
	 * @param model Elytra model
	 * @param matrices Transformation matrices
	 * @param provider Vertex consumer provider duh
	 * @param entity Elytra wearer or null
	 * @param stack Elytra item stack
	 * @param light Lightmap coordinates
	 * @param red Red color value
	 * @param green Green color value
	 * @param blue Blue color value
	 * @param alpha Alpha value
	 */
	public static void renderFeatures(
			@NotNull Model model,
			@NotNull MatrixStack matrices,
			@NotNull VertexConsumerProvider provider,
			@Nullable LivingEntity entity,
			@NotNull ItemStack stack,
			int light,
			float red,
			float green,
			float blue,
			float alpha) {
		int color = ColorKt.toARGB(red, green, blue, alpha);
		renderFeatures(model, matrices, provider, entity, stack, light, color);
	}

	/**
	 * Provides feature rendering access for custom elytras.
	 *
	 * @param model Elytra model
	 * @param matrices Transformation matrices
	 * @param provider Vertex consumer provider duh
	 * @param entity Elytra wearer or null
	 * @param stack Elytra item stack
	 * @param light Lightmap coordinates
	 * @param color ARGB color integer
	 */
	public static void renderFeatures(
			@NotNull Model model,
			@NotNull MatrixStack matrices,
			@NotNull VertexConsumerProvider provider,
			@Nullable LivingEntity entity,
			@NotNull ItemStack stack,
			int light,
			int color) {
		ETRenderer.render(model, matrices, provider, entity, stack, light, color);
	}

	/**
	 * Checks if the player has elytra cape rendering enabled in their config.
	 *
	 * @param entity Entity to be rendered
	 * @return false if cape rendering is disabled in config
	 */
	public static boolean shouldShowCape(@NotNull LivingEntity entity) {
		return ETRenderer.shouldRender(RenderType.CAPE, entity);
	}

	/**
	 * Checks if the item is supposed to be an elytra.
	 * <br>
	 * Implementations are quite inconsistent across mods, so it checks the following criterias:
	 * <li>Item extends {@link ElytraItem}.</li>
	 * <li>Item implements <a href="https://github.com/FabricMC/fabric/blob/1.21/fabric-entity-events-v1/src/main/java/net/fabricmc/fabric/api/entity/event/v1/FabricElytraItem.java">FabricElytraItem</a>.</li>
	 * <li>Item extends {@link ArmorItem} and contains "elytra" in its id.</li>
	 * @param stack Item stack to check
	 * @return true if this item should be an elytra
	 */
	public static boolean isProbablyElytra(@NotNull ItemStack stack) {
		return isProbablyElytra(stack.getItem());
	}

	/**
	 * Checks if the item is supposed to be an elytra.
	 * <br>
	 * Implementations are quite inconsistent across mods, so it checks the following criterias:
	 * <li>Item extends {@link ElytraItem}.</li>
	 * <li>Item implements <a href="https://github.com/FabricMC/fabric/blob/1.21/fabric-entity-events-v1/src/main/java/net/fabricmc/fabric/api/entity/event/v1/FabricElytraItem.java">FabricElytraItem</a>.</li>
	 * <li>Item extends {@link ArmorItem} and contains "elytra" in its id.</li>
	 *
	 * @param item Item to check
	 * @return true if this item should be an elytra
	 */
	public static boolean isProbablyElytra(@NotNull Item item) {
		return UtilKt.isProbablyElytra(item);
	}
}