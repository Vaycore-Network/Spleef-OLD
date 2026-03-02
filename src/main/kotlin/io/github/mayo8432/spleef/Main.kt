package io.github.mayo8432.spleef

import de.c4vxl.gamemanager.gma.game.Game
import de.c4vxl.gamemanager.language.Language
import de.c4vxl.gamemanager.utils.ResourceUtils
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIPaperConfig
import io.github.mayo8432.spleef.handler.BlockBreakHandler
import io.github.mayo8432.spleef.handler.GameHandler
import io.github.mayo8432.spleef.handler.InteractionHandler
import io.github.mayo8432.spleef.handler.ProjectileHandler
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger

class Main : JavaPlugin() {
    companion object {
        lateinit var instance: Main
        lateinit var logger: Logger
    }

    override fun onLoad() {
        instance = this
        Main.logger = this.logger

        // Load CommandAPI
        CommandAPI.onLoad(
            CommandAPIPaperConfig(this)
                .silentLogs(true)
                .verboseOutput(false)
        )
    }

    override fun onEnable() {
        // Enable CommandAPI
        CommandAPI.onEnable()

        // Register language extensions
        ResourceUtils.readResource("langs", Main::class.java).split("\n")
            .forEach { langName ->
                Language.provideLanguageExtension(
                    "spleef",
                    langName,
                    ResourceUtils.readResource("lang/$langName.yml", Main::class.java)
                )
            }

        logger.info("[+] $name has been enabled!")

        // Introducing Handlers
        GameHandler()
        BlockBreakHandler()
        InteractionHandler()
        ProjectileHandler()
    }

    override fun onDisable() {
        // Disable CommandAPI
        CommandAPI.onDisable()

        logger.info("[+] $name has been disabled!")
    }
}