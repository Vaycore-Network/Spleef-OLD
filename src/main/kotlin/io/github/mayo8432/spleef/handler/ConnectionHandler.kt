package io.github.mayo8432.spleef.handler

import de.c4vxl.gamemanager.gma.GMA
import de.c4vxl.gamemanager.gma.event.player.GamePlayerQuitEvent
import de.c4vxl.gamemanager.gma.game.type.GameSize
import de.c4vxl.gamemanager.gma.player.GMAPlayer
import de.c4vxl.gamemanager.gma.player.GMAPlayer.Companion.gma
import io.github.mayo8432.spleef.Main
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

/**
 * Skips the lobby and puts a player directly into a queue
 */
class ConnectionHandler : Listener {
    init {
        Bukkit.getPluginManager().registerEvents(this, Main.instance)
    }

    /**
     * Puts a player into a game
     */
    private fun join(player: GMAPlayer) {
        player.join(GMA.getOrCreate(GameSize(12, 1)))
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        Bukkit.getScheduler().callSyncMethod(Main.instance) {
            join(event.player.gma)
        }
    }

    @EventHandler
    fun onQuit(event: GamePlayerQuitEvent) {
        Bukkit.getScheduler().callSyncMethod(Main.instance) {
            join(event.player)
        }
    }
}