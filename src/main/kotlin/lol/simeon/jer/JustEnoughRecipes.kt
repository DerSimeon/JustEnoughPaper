package lol.simeon.jer

import lol.simeon.jer.listeners.ConnectionHandler
import lol.simeon.jer.listeners.FabricNettyInterceptor
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class JustEnoughRecipes : JavaPlugin() {

    override fun onEnable() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "neoforge:recipe_content")
        ConnectionHandler(this)
        FabricNettyInterceptor(this)
    }
}