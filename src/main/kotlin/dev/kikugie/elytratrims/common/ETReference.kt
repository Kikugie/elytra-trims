package dev.kikugie.elytratrims.common

import dev.kikugie.elytratrims.common.util.identifier
import org.slf4j.LoggerFactory

object ETReference {
    const val MOD_ID: String = "elytratrims"
    @JvmField
    val LOGGER = LoggerFactory.getLogger(MOD_ID)

    fun id(path: String) = identifier(MOD_ID, path)
}