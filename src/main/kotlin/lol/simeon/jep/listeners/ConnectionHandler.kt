package lol.simeon.jep.listeners

import lol.simeon.jep.JustEnoughPaper
import lol.simeon.jep.protocol.PacketBuilder
import lol.simeon.jep.protocol.RecipeEntryBuilder
import lol.simeon.jep.protocol.packet.RecipeSyncPayload
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class ConnectionHandler(val instance: JustEnoughPaper) : Listener {

    init {
        Bukkit.getPluginManager().registerEvents(this, instance)
    }

    @EventHandler
    fun onConnect(event: PlayerJoinEvent) {
        PacketBuilder.sendRecipeSyncAsDiscarded(event.player, RecipeSyncPayload(RecipeEntryBuilder.buildRecipeSyncEntries()))
    }
}