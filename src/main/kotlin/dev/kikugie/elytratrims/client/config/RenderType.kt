package dev.kikugie.elytratrims.client.config

import dev.kikugie.elytratrims.client.translation

enum class RenderType {
    COLOR, PATTERNS, TRIMS, CAPE, GLOW, GLOBAL;
    val text get() = "elytratrims.config.type.${name.lowercase()}".translation()
    val tooltip get() = "elytratrims.config.type.${name.lowercase()}.tooltip".translation()
}