package io.github.mayo8432.spleef.handler

import de.c4vxl.gamelobby.events.queue.LobbyPlayerQueueJoinedEvent
import de.c4vxl.gamemanager.gma.GMA
import io.github.mayo8432.spleef.Main
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class LobbyHandler : Listener {
    init {
        Bukkit.getPluginManager().registerEvents(this, Main.instance)

        var i = 0
        Bukkit.getScheduler().runTaskTimer(Main.instance, Runnable {
            GMA.registeredGames.forEach { game ->
                if (!game.isQueuing)
                    return@forEach

                game.players.forEach {
                    val language = it.language.child("spleef")

                    it.bukkitPlayer.sendActionBar(language.getCmp("queue.waiting",
                        ".".repeat(i + 1), (game.players.size - 1).toString(), (game.size.maxPlayers - 1).toString()
                    ))
                }
            }

            i = if (i < 2) i+1 else 0
        }, 0, 20)
    }

    @EventHandler
    fun onEquip(event: LobbyPlayerQueueJoinedEvent) {
        val chooserItem = event.player.inventory.getItem(1)

        event.player.inventory.clear()
        event.player.inventory.setItem(4, chooserItem)
    }
}