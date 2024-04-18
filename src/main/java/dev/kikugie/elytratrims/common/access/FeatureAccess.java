package dev.kikugie.elytratrims.common.access;

import com.mojang.datafixers.util.Pair;
import dev.kikugie.elytratrims.common.plugin.ModStatus;
import io.github.apfelrauber.stacked_trims.ArmorTrimList;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.item.BannerItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.DyeColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/*? if >1.20.4 {*//*
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.component.type.NbtComponent;
*//*?} */

import static java.util.Collections.emptyList;

@SuppressWarnings("CommentedOutCode")
public class FeatureAccess {
    /*? if <=1.20.4*/
    private static final net.minecraft.item.DyeableItem DYEABLE = new net.minecraft.item.DyeableItem() {};

    public static List<BannerLayer> getPatterns(ItemStack stack) {
        /*? if <=1.20.4 {*/
        NbtList patterns = BannerBlockEntity.getPatternListNbt(stack);
        if (patterns == null) return emptyList();

        NbtCompound nbt = BlockItem.getBlockEntityNbt(stack);
        if (nbt == null) return emptyList();
        DyeColor baseColor = nbt.contains("Base") ? DyeColor.byId(nbt.getInt("Base")) : DyeColor.WHITE;
        List<BannerLayer> out = new ArrayList<>();
        for (Pair<RegistryEntry<BannerPattern>, DyeColor> entry : BannerBlockEntity.getPatternsFromNbt(baseColor, patterns))
            out.add(new BannerLayer(entry.getFirst(), entry.getSecond()));
        return out;
        /*?} else {*//*
        var patterns = stack.get(DataComponentTypes.BANNER_PATTERNS).layers();
        if (patterns == null || patterns.isEmpty()) return emptyList();

        List<BannerLayer> out = new ArrayList<>();
        for (BannerPatternsComponent.Layer layer : patterns)
            out.add(new BannerLayer(layer.pattern(), layer.color()));
        return out;
        *//*?} */
    }

    public static void setPatterns(ItemStack target, ItemStack source) {
        assert source.getItem() instanceof BannerItem;
        /*? if <=1.20.4 {*/
        NbtList patterns = BannerBlockEntity.getPatternListNbt(source);
        if (patterns != null) {
            DyeColor color = ((BannerItem) source.getItem()).getColor();
            NbtCompound storage = target.getOrCreateSubNbt("BlockEntityTag");
            storage.put("Patterns", patterns);
            storage.put("Base", NbtInt.of(color.getId()));
            setColor(target, color.getFireworkColor());
        } else if (target.getSubNbt("BlockEntityTag") != null && target.getSubNbt("BlockEntityTag").isEmpty()) {
            target.removeSubNbt("BlockEntityTag");
            setColor(target, 0);
        }
        /*?} else {*//*
        var patterns = source.getComponents().filtered(t -> t == DataComponentTypes.BANNER_PATTERNS);
        target.applyComponentsFrom(patterns);
        *//*?} */
    }

    public static int getColor(ItemStack stack) {
        /*? if <=1.20.4*/
        return DYEABLE.hasColor(stack) ? DYEABLE.getColor(stack) : 0;
        /*? if >1.20.4*/
        /*return DyedColorComponent.getColor(stack, 0);*/
    }

    public static void setColor(ItemStack target, int color) {
        /*? if <=1.20.4 {*/
        if (color != 0) DYEABLE.setColor(target, color);
        else DYEABLE.removeColor(target);
        /*?} else {*/
        /*target.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(color, true));*/
        /*?} */
    }

    public static List<ArmorTrim> getTrims(ItemStack stack, DynamicRegistryManager registry) {
        /*? if <=1.20.4 {*/
        if (ModStatus.isLoaded("stacked-armor-trims"))
            return ArmorTrimList.getTrims(registry, stack).orElse(emptyList());
        Optional<ArmorTrim> trim = ArmorTrim.getTrim(registry, stack);
        return trim.map(Collections::singletonList).orElse(emptyList());
        /*?} else {*//*
        var trim = stack.get(DataComponentTypes.TRIM);
        return trim != null ? Collections.singletonList(trim) : emptyList();
        *//*?} */
    }

    public static boolean hasGlow(ItemStack stack) {
        /*? if <=1.20.4 {*/
        NbtCompound nbtCompound = stack.getSubNbt("display");
        return nbtCompound != null && nbtCompound.contains("glow", 1) && nbtCompound.getBoolean("glow");
        /*?} else {*//*
        var data = stack.get(DataComponentTypes.CUSTOM_DATA);
        return data != null && data.getNbt().getBoolean("glow");
        *//*?} */
    }

    public static void setGlow(ItemStack stack, boolean glow) {
        /*? if <=1.20.4 {*/
        if (glow) stack.getOrCreateSubNbt("display").putBoolean("glow", true);
        else {
            NbtCompound nbtCompound = stack.getSubNbt("display");
            if (nbtCompound != null && nbtCompound.contains("color")) {
                nbtCompound.remove("glow");
            }
        }
        /*?} else {*//*
        var data = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (data == null && glow) {
            data = NbtComponent.DEFAULT;
            stack.set(DataComponentTypes.CUSTOM_DATA, data);
        }
        if (glow) data.getNbt().putBoolean("glow", true);
        else data.getNbt().remove("glow");
        *//*?} */
    }
}