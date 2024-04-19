package dev.kikugie.elytratrims.client.resource

import com.google.gson.JsonParser
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.serialization.JsonOps
import dev.kikugie.elytratrims.common.ETReference
import dev.kikugie.elytratrims.common.util.getAnyway
import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.*
import net.minecraft.client.texture.SpriteLoader.StitchResult
import net.minecraft.client.texture.atlas.AtlasLoader
import net.minecraft.client.texture.atlas.PalettedPermutationsAtlasSource
import net.minecraft.resource.ResourceFinder
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceReloader
import net.minecraft.util.Identifier
import net.minecraft.util.profiler.Profiler
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.function.Function
import java.util.function.Supplier

object ETAtlasHolder : ResourceReloader {
    /*? if >=1.20.2 */
    private val opener: SpriteOpener = SpriteOpener.create(SpriteLoader.METADATA_READERS)
    val id: Identifier = ETReference.id("elytra_features")
    val atlas = SpriteAtlasTexture(ETReference.id("textures/atlas/elytra_features.png")).also {
        RenderSystem.recordRenderCall { MinecraftClient.getInstance().textureManager.registerTexture(id, it) }
    }
    var ready = false
        private set

    private fun sprites(manager: ResourceManager, model: NativeImage, id: Identifier) = buildList {
        addAll(trims(manager, model))
        addAll(patterns(manager, model))
        add(color(id, model))
        add(MissingSprite::createSpriteContents)
    }

    private fun patterns(manager: ResourceManager, model: NativeImage): Collection<ContentSupplier> {
        val useBanners = false
        val finder = ResourceFinder("textures/entity/${if (useBanners) "banner" else "shield"}", ".png")
        return buildList {
            for ((id, res) in finder.findResources(manager)) {
                var image = AtlasSprite(finder.toResourceId(id), res, 1).readSafe() ?: continue
                image = image.use {
                    if (useBanners) it.cropped(it.width * 2)
                    else it.cropped(height = it.height / 2)
                }
                val scale: Int = image.width / 64
                val xOffset = ((if (useBanners) 35.5F else 34F) * scale).toInt()
                val yOffset = if (useBanners) (scale * 1.5F).toInt() else 0
                add {
                    image.use { it.offset(xOffset, yOffset) }
                        .use { it.mask(model) }
                        .toContents(id)
                }
            }
        }
    }

    private fun trims(manager: ResourceManager, model: NativeImage): Collection<ContentSupplier> {
        val crop = true
        val atlases = manager.findResources("atlases") { it.path.endsWith("armor_trims.json") }
        val sources = atlases.mapNotNull { (_, v) ->
            try {
                PalettedPermutationsAtlasSource.CODEC
                    /*? if >1.20.4*/.codec()
                    .decode(JsonOps.INSTANCE, JsonParser.parseReader(v.reader)).getAnyway().first
            } catch (e: Exception) {
                null
            }
        }
        return AtlasLoader(sources).loadSources(manager).map { {
            /*? if >=1.20.2 */
            if (crop) it.apply(opener).transform { it.mask(model) } else it.apply(opener)
            /*? if <1.20.2 */
            /*it.get().transform { it.mask(model) }*/
        } }
    }

    private fun color(id: Identifier, model: NativeImage): ContentSupplier = { saturationMask(model).toContents(id) }

    private fun transform(
        sprites: List<ContentSupplier>,
        executor: Executor,
    ): CompletableFuture<List<SpriteContents>> =
        /*? if <1.20.2 {*//*
        SpriteLoader.loadAll(sprites.map { Supplier { it() } }, executor);
        *//*?} else {*/
        SpriteLoader.loadAll(opener, sprites.map { Function { it() } }, executor)
        /*?} */

    private fun load(manager: ResourceManager, executor: Executor): CompletableFuture<StitchResult> {
        var model: NativeImage? = null
        return CompletableFuture.supplyAsync {
            ready = false
            atlas.clear()
            val id = Identifier("textures/entity/elytra.png")
            model = loadTexture(id, manager)?.readSafe() ?: return@supplyAsync emptyList()
            sprites(manager, model!!, id.withPath { path: String ->
                path.replace("textures/", "").replace(".png", "")
            })
        }.thenCompose {
            transform(it, executor)
        }.thenApply {
            SpriteLoader.fromAtlas(atlas).stitch(it, 0, executor)
        }.thenCompose(StitchResult::whenComplete)
            .whenComplete { _, _ -> model?.close() }
    }

    fun apply(
        data: StitchResult,
        profiler: Profiler,
        executor: Executor,
    ): CompletableFuture<Void> = CompletableFuture.runAsync({
        profiler.startTick()
        profiler.push("upload")
        atlas.upload(data)
        ready = true
        profiler.pop()
        profiler.endTick()
    }, executor)

    override fun reload(
        helper: ResourceReloader.Synchronizer,
        manager: ResourceManager,
        loadProfiler: Profiler,
        applyProfiler: Profiler,
        loadExecutor: Executor,
        applyExecutor: Executor,
    ): CompletableFuture<Void> = load(manager, loadExecutor)
        .thenCompose {
            helper.whenPrepared(it)
        }.thenCompose {
            apply(it, applyProfiler, applyExecutor)
        }
}