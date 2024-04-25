package dev.kikugie.elytratrims.mixin.access;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface ElytraRotationAccessor {
    Vector3f elytratrims$UP = new Vector3f(0, 1, 0);
    Quaternionf elytratrims$getVector();
    boolean elytratrims$isElytra();
    void elytratrims$setElytra(boolean value);

    default Quaternionf elytratrims$rotateElytra(Quaternionf source) {
        if (elytratrims$isElytra()) {
            Quaternionf vec = elytratrims$getVector();
            source.rotateAxis((float) Math.PI, elytratrims$UP, vec);
            return vec;
        } else return source;
    }
}
