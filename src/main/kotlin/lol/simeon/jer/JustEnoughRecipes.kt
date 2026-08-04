package lol.simeon.jer

import lol.simeon.jer.listeners.ConnectionHandler
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class JustEnoughRecipes : JavaPlugin() {

    override fun onEnable() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "neoforge:recipe_content")
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "fabric:recipe_sync")
        ConnectionHandler(this)
    }
}