package dev.kikugie.elytratrims.platform

/*? if fabric {*/
import dev.kikugie.elytratrims.client.ETClient
import dev.kikugie.elytratrims.client.resource.ETAtlasHolder
import dev.kikugie.elytratrims.common.ETReference
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.ResourcePackActivationType
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceReloader
import net.minecraft.resource.ResourceType
import net.minecraft.text.Text
import net.minecraft.util.profiler.Profiler
import java.util.concurrent.Executor

object ETClientWrapper : ClientModInitializer {
    override fun onInitializeClient() {
        ETClient.init()
        ResourceManagerHelper.registerBuiltinResourcePack(
            ETReference.id("legacy"),
            FabricLoader.getInstance().getModContainer(ETReference.MOD_ID).get(),
            Text.translatable("elytratrims.legacy_rp"),
            ResourcePackActivationType.NORMAL
        )
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(object :
            IdentifiableResourceReloadListener {
            override fun reload(
                synchronizer: ResourceReloader.Synchronizer,
                manager: ResourceManager,
                prepareProfiler: Profiler,
                applyProfiler: Profiler,
                prepareExecutor: Executor,
                applyExecutor: Executor,
            ) = ETAtlasHolder.reload(
                synchronizer,
                manager,
                prepareProfiler,
                applyProfiler,
                prepareExecutor,
                applyExecutor
            )

            override fun getFabricId() = ETReference.id("textures")
        })
    }
}
/*?}*/