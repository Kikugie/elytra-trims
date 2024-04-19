package dev.kikugie.elytratrims.client

import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

val CLIENT: MinecraftClient = MinecraftClient.getInstance()

fun String.translation(vararg args: String): Text = Text.translatable(this, *args)