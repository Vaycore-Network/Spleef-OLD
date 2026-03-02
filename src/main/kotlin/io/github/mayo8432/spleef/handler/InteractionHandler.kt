package io.github.mayo8432.spleef.handler

import de.c4vxl.gamemanager.gma.player.GMAPlayer.Companion.gma
import de.c4vxl.gamemanager.language.Language
import io.github.mayo8432.spleef.Main
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class InteractionHandler : Listener {

    init {
        Bukkit.getPluginManager().registerEvents(this, Main.instance)
    }

    @EventHandler
    fun onRightClick(event: PlayerInteractEvent) {

        // Checking if the player is currently in a game (Should only apply inside a game)
        if (!event.player.gma.isInGame) return

        // Checking if the player right-clicked a golden shovel
        if (event.item?.type == Material.GOLDEN_SHOVEL && (event.action == Action.RIGHT_CLICK_BLOCK || event.action == Action.RIGHT_CLICK_AIR)) {

            // Ensures that the player cant shoot snowballs he doesn't have
            if (!event.player.inventory.contains(Material.SNOWBALL)) return

            // Launching the snowball projectile and removing the item from player inventory
            event.player.launchProjectile(Snowball::class.java)
            event.player.inventory.removeItemAnySlot(ItemStack(Material.SNOWBALL))
        }
    }

    @EventHandler
    fun onDrop(event: PlayerDropItemEvent) {

        // Returns when the player is not in a game
        if (!event.player.gma.isInGame) return

        // Cancelling the drop event
        event.isCancelled = true
    }

    @EventHandler
    fun onDamageByEntity(event: EntityDamageByEntityEvent) {

        // Only react, if the victim is a player
        val player = event.entity as? Player ?: return

        // If the player is not in game → allow damage
        if (!player.gma.isInGame) return

        // Blocking out the damage
        event.isCancelled = true
    }

    @EventHandler
    fun onCraft(event: CraftItemEvent) {

        // Check whether the user is a player
        val player = event.whoClicked as? Player ?: return

        // @param Getting the players language
        val language = player.gma.language
        // @param Message using the multi-language support
        val message = language.child("spleef").getCmp("message.cantcraft")

        if (player.gma.isInGame) {
            event.isCancelled = true
            player.sendMessage(message)
        }
    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {

        // Check whether the entity is a player
        val player = event.entity as? Player ?: return

        // Returns when the player is not in a game
        if (!player.gma.isInGame) return

        // Checks whether the player is a Spectator
        if (player.gma.isSpectating) return

        // Checking whether the player is damaged by lava
        if (event.cause == EntityDamageEvent.DamageCause.LAVA || event.cause == EntityDamageEvent.DamageCause.VOID) {

            // Canceling damage
            event.isCancelled = true

            // Eliminating the player after 1 tick
            Bukkit.getScheduler().runTaskLater(Main.instance, Runnable {
                player.gma.eliminate()
            },1L)
        }
    }
}