package dev.kikugie.elytratrims.mixin.plugin;

import dev.kikugie.elytratrims.common.config.Tester;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireTest {
    Class<? extends Tester> value();
}
