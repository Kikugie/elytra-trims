package dev.kikugie.elytratrims.common.config

import dev.kikugie.elytratrims.common.ETReference
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

@OptIn(ExperimentalSerializationApi::class)
object ETConfigLoader {
    val json = Json { isLenient = true; ignoreUnknownKeys = true }

    inline fun <reified T> load(file: Path, default: () -> T): T {
        if (file.exists()) try {
            return json.decodeFromStream(file.inputStream())
        } catch (e: Exception) {
            ETReference.LOGGER.error("Failed to read config $file", e)
        }
        return default().also { save(file, it) }
    }

    inline fun <reified T> save(file: Path, instance: T) = try {
        file.parent.createDirectories()
        json.encodeToStream(instance, file.outputStream())
    } catch (e: Exception) {
        ETReference.LOGGER.error("Failed to save config $file", e)
    }
}