package dev.kikugie.elytratrims.platform

import java.nio.file.Path

/*? if fabric {*/
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader


object ModStatus : IModStatus {
    private val fabric = FabricLoader.getInstance();
    override val isClient = fabric.environmentType == EnvType.CLIENT
    override val isDev = fabric.isDevelopmentEnvironment
    override val configDir = fabric.configDir

    override fun isLoaded(mod: String) = fabric.isModLoaded(mod)
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
    private val cache = Object2BooleanOpenHashMap<String>()

    override fun isLoaded(mod: String) = cache.computeIfAbsent(mod, Predicate {FMLLoader.getLoadingModList().getModFileById(mod) != null})
}
  *//*?} else {*//*
import net.neoforged.fml.loading.FMLLoader
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
    private val cache = Object2BooleanOpenHashMap<String>()
    override fun isLoaded(mod: String) = cache.computeIfAbsent(mod, Predicate {FMLLoader.getLoadingModList().getModFileById(mod) != null})

}
  *//*?}*/

private interface IModStatus {
    val isClient: Boolean
    val isDev: Boolean
    val configDir: Path

    fun isLoaded(mod: String): Boolean
}