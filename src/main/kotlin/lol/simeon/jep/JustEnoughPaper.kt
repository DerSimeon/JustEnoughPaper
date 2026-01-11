package lol.simeon.jep

import lol.simeon.jep.listeners.ConnectionHandler
import org.bukkit.plugin.java.JavaPlugin

class JustEnoughPaper : JavaPlugin() {

    override fun onEnable() {
        logger.info("Starting JustEnoughPaper v${pluginMeta.version}")
        ConnectionHandler(this)
    }
}