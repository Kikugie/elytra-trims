package dev.kikugie.elytratrims.common

import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory

object ETReference {
    const val MOD_ID: String = "elytratrims"
    @JvmField
    val LOGGER = LoggerFactory.getLogger(MOD_ID)

    fun id(path: String) = Identifier(MOD_ID, path)
}