package dev.kikugie.elytratrims.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.elytratrims.common.util.UtilKt;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.registry.tag.TagManagerLoader;
import net.minecraft.server.DataPackContents;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;
import java.util.Map;

@Mixin(DataPackContents.class)
public abstract class DataPackContentsMixin {
	@Unique
	private static final Identifier key = UtilKt.identifier("item");

	@ModifyArg(method = "repopulateTags", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/Registry;populateTags(Ljava/util/Map;)V"))
	private static Map<TagKey<?>, List<RegistryEntry<?>>> insertElytraTags(Map<TagKey<?>, List<RegistryEntry<?>>> tagEntries, @Local(argsOnly = true) TagManagerLoader.RegistryTags<?> tags) {
		return key.equals(tags.key().getValue()) ? UtilKt.populateTags(tagEntries) : tagEntries;
	}
}