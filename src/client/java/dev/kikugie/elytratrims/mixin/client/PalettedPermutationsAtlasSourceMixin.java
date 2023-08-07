package dev.kikugie.elytratrims.mixin.client;

import dev.kikugie.elytratrims.ElytraTrimsMod;
import dev.kikugie.elytratrims.access.ElytraSourceAccessor;
import net.minecraft.client.texture.atlas.AtlasSource;
import net.minecraft.client.texture.atlas.PalettedPermutationsAtlasSource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Generates textures for every occuring palette. Unfortunately for every occuring pattern as well, which may cause some log spam.
 */
@Mixin(PalettedPermutationsAtlasSource.class)
public class PalettedPermutationsAtlasSourceMixin implements ElytraSourceAccessor {
    @Unique
    private final String SUPPORTED_PATTERN = "trims/models/armor/[\\w_-]+";
    @Shadow
    @Final
    public List<Identifier> textures;
    @Shadow
    @Final
    public Identifier paletteKey;
    @Shadow
    @Final
    public Map<String, Identifier> permutations;
    @Unique
    private boolean elytra = false;

    @Inject(method = "load", at = @At("HEAD"))
    private void loadElytraPermutations(ResourceManager resourceManager, AtlasSource.SpriteRegions regions, CallbackInfo ci) {
        if (this.elytra)
            return;
        List<Identifier> elytraTextures = new ArrayList<>(this.textures.size());
        for (Identifier texture : this.textures) {
            String path = texture.getPath();
            if (path.contains("armor")
                    && !path.contains("leggings")
                    && path.matches(this.SUPPORTED_PATTERN))
                elytraTextures.add(ElytraTrimsMod.id(path.replaceFirst("armor", "elytra")));
        }
        if (elytraTextures.isEmpty())
            return;

        PalettedPermutationsAtlasSource elytraSource = new PalettedPermutationsAtlasSource(elytraTextures, this.paletteKey, this.permutations);
        ((ElytraSourceAccessor) elytraSource).elytra_trims$enableElytra();
        elytraSource.load(resourceManager, regions);
    }

    @Unique
    @Override
    public void elytra_trims$enableElytra() {
        this.elytra = true;
    }
}