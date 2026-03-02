package io.github.mayo8432.spleef.handler

import de.c4vxl.gamemanager.gma.event.game.GameStartedEvent
import de.c4vxl.gamemanager.gma.event.player.GamePlayerRespawnEvent
import de.c4vxl.gamemanager.gma.game.Game
import de.c4vxl.gamemanager.gma.player.GMAPlayer.Companion.gma
import de.c4vxl.gamemanager.utils.ItemBuilder
import io.github.mayo8432.spleef.Main
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.TitlePart
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import kotlin.collections.contains
import kotlin.collections.remove

class GameHandler : Listener {

    init {
        Bukkit.getPluginManager().registerEvents(this, Main.instance)
    }

    // @param A list of the games that are in the starting state
    companion object {
        val startingGames = mutableListOf<Game>()
    }

    @EventHandler
    fun onRespawn(event: GamePlayerRespawnEvent) {
        // Kills the player on respawn (1 live)
        event.player.eliminate(event.killer)    // event.killer is used for the GMA Leaderboard implementation
    }

    @EventHandler
    fun onGameStart (event: GameStartedEvent) {

        // @param All alive players currently in the game
        val players = event.game.playerManager.alivePlayers
        // @param The amount of seconds before the game starts
        val seconds = 5

        // Setting the current game into the starting game state
        startingGames.add(event.game)

        players.forEach {

            // Giving each player a shovel with game description on game start (Using GMA's multi-language support)
            val namedShovel = ItemBuilder(Material.GOLDEN_SHOVEL, name = it.language.child("spleef").getCmp("shovel.name")).build()
            it.bukkitPlayer.inventory.addItem(namedShovel)
        }

        // This code snippet is used to show the player the time until a game starts
        for (i in seconds downTo 1) {
            Bukkit.getScheduler().runTaskLater(Main.instance, Runnable {
                players.forEach {
                    it.bukkitPlayer.sendTitlePart(TitlePart.TITLE, Component.text("$i.."))
                }
            }, (seconds - i) * 20L)   // *20L due to 20 Minecraft Ticks being 1 second
        }

        // This runTaskLater removes the UI Title Countdown and removes the starting state from the game
        Bukkit.getScheduler().runTaskLater(Main.instance, Runnable {

            startingGames.remove(event.game)
            event.game.playerManager.alivePlayers.forEach {
                it.bukkitPlayer.clearTitle()
            }
        }, seconds * 20L)
    }

}