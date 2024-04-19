package dev.kikugie.elytratrims.mixin.client;

import net.minecraft.client.texture.atlas.PalettedPermutationsAtlasSource;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(PalettedPermutationsAtlasSource.class)
public interface PalettedPermutationsAtlasSourceAccessor {
    @Accessor
    List<Identifier> getTextures();

    @Mutable
    @Accessor
    void setTextures(List<Identifier> textures);
}
