package dev.kikugie.elytratrims.extensions;

import io.github.andrew6rant.dynamictrim.api.DynamicTrimRegistry;
import io.github.andrew6rant.dynamictrim.resource.TrimmableItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ExtensionsMod implements ClientModInitializer {
    public static final String MOD_ID = "elytratrims_extensions";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
//        List<PackEntry> packs = List.of(
//                new PackEntry("more_armor_trims", "more_armor_trims_legacy", "Legacy More Armor Trims")
//        );
//
//        var fabric = FabricLoader.getInstance();
//        var container = fabric.getModContainer(MOD_ID).get();
//        for (PackEntry pack : packs) {
//            if (!fabric.isModLoaded(pack.mod())) continue;
//            LOGGER.info("Registering extension pack: {}", pack.path());
//            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(MOD_ID, pack.path()),
//                    container,
//                    ResourcePackActivationType.NORMAL
//            );
//        }
//
//        if (fabric.isModLoaded("dynamictrim"))
//            DynamicTrimRegistry.add(new TrimmableItem("elytra", new Identifier("elytra")));
    }
}