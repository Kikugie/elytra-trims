package dev.kikugie.elytratrims.client.config.lib

/*? if fabric {*/
import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import net.minecraft.client.gui.screen.Screen

object ModMenuImpl : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<out Screen> =
        ConfigScreenFactory(ConfigScreenProvider::open)
}
/*?}*/