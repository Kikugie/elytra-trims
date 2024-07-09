package dev.kikugie.elytratrims.platform

import java.nio.file.Path

/*? if fabric {*/
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import kotlin.jvm.optionals.getOrNull


object ModStatus : IModStatus {
    private val fabric = FabricLoader.getInstance()
    override val isClient = fabric.environmentType == EnvType.CLIENT
    override val isDev = fabric.isDevelopmentEnvironment
    override val configDir = fabric.configDir
    override val platform = "fabric"

    override fun isLoaded(mod: String) = fabric.isModLoaded(mod)
    override fun getVersion(mod: String) = fabric.getModContainer(mod).getOrNull()?.metadata?.version?.friendlyString
}
/*?} elif forge {*/
/*import net.minecraftforge.fml.loading.FMLLoader
import java.util.function.Predicate
import kotlin.io.path.createDirectories
import kotlin.io.path.notExists
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap

object ModStatus : IModStatus {
    override val isClient = FMLLoader.getDist().isClient
    override val isDev = !FMLLoader.isProduction()
    override val configDir: Path = FMLLoader.getGamePath().resolve("config").also {
        if (it.notExists()) it.createDirectories()
    }
    override val platform = "forge"
    private val cache = Object2BooleanOpenHashMap<String>()

    override fun isLoaded(mod: String) = cache.computeIfAbsent(mod, Predicate {FMLLoader.getLoadingModList().getModFileById(mod) != null})
    override fun getVersion(mod: String): String? = FMLLoader.getLoadingModList().getModFileById(mod)?.versionString()
}
*//*?} else {*/
/*import net.neoforged.fml.loading.FMLLoader
import kotlin.io.path.createDirectories
import kotlin.io.path.notExists
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap
import java.util.function.Predicate

object ModStatus : IModStatus {
    override val isClient = FMLLoader.getDist().isClient
    override val isDev = !FMLLoader.isProduction()
    override val configDir: Path = FMLLoader.getGamePath().resolve("config").also {
        if (it.notExists()) it.createDirectories()
    }
    override val platform = "neoforge"
    private val cache = Object2BooleanOpenHashMap<String>()
    override fun isLoaded(mod: String) = cache.computeIfAbsent(mod, Predicate {FMLLoader.getLoadingModList().getModFileById(mod) != null})
    override fun getVersion(mod: String): String? = FMLLoader.getLoadingModList().getModFileById(mod)?.versionString()
}
  *//*?}*/

private interface IModStatus {
    val isClient: Boolean
    val isDev: Boolean
    val configDir: Path
    val platform: String
    val mcVersion get() = /*$ mc >>*/ "1.20.1"

    fun isLoaded(mod: String): Boolean
    fun getVersion(mod: String): String?
}