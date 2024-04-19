package dev.kikugie.elytratrims.common.config

import com.google.gson.JsonParser
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import dev.kikugie.elytratrims.common.ETReference
import dev.kikugie.elytratrims.common.util.getAnyway
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.function.Supplier

object ConfigLoader {
    @Throws(IOException::class)
    fun <T> load(file: Path, codec: Codec<T>, provider: Supplier<T>): T {
        if (!Files.exists(file)) {
            Files.createDirectories(file.parent)
            val newConfig = provider.get()
            save(file, codec, newConfig)
            return newConfig
        }
        try {
            val json = JsonParser.parseReader(Files.newBufferedReader(file))
            return codec.decode(JsonOps.INSTANCE, json).getAnyway().first
        } catch (e: Exception) {
            ETReference.LOGGER.warn("Failed to read config: $e")
            val newConfig = provider.get()
            save(file, codec, newConfig)
            return newConfig
        }
    }

    fun <T> save(file: Path, codec: Codec<T>, instance: T) {
        try {
            val result = codec.encodeStart(JsonOps.INSTANCE, instance).getAnyway()
            Files.writeString(
                file,
                result.toString(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            )
        } catch (e: Exception) {
            ETReference.LOGGER.warn("Failed to save config to $file:\n$e")
        }
    }
}
