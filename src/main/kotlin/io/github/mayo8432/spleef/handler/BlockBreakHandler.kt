package io.github.mayo8432.spleef.handler

import de.c4vxl.gamemanager.gma.player.GMAPlayer.Companion.gma
import io.github.mayo8432.spleef.Main
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack

class BlockBreakHandler : Listener {

    init {
        Bukkit.getPluginManager().registerEvents(this, Main.instance)
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {

        // Checking if the player is currently in a game (Should only apply inside a game)
        if (!event.player.gma.isInGame) return

        /*  Checking whether the broken block is a snow block
            Checking whether the game is in the starting game state
         */
        if (event.block.type != Material.SNOW_BLOCK || event.player.gma.game in GameHandler.startingGames) {
            event.isCancelled = true
            return
        }

        // Disabling item drops and swapping the block into air (No durability loss)
        event.isDropItems = false
        event.block.type = Material.AIR

        // Adding 2 snowballs into the players inventory
        event.player.inventory.addItem(ItemStack(Material.SNOWBALL, 2))
    }
}