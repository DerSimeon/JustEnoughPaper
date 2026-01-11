package lol.simeon.jep.boot

import io.papermc.paper.plugin.bootstrap.BootstrapContext
import io.papermc.paper.plugin.bootstrap.PluginBootstrap
import io.papermc.paper.plugin.bootstrap.PluginProviderContext
import lol.simeon.jep.JustEnoughPaper
import org.bukkit.plugin.java.JavaPlugin

@Suppress("UnstableApiUsage")
public class JepBootstrapper : PluginBootstrap {

    /**
     * bootstrap the plugin
     * @param bootstrapContext the bootstrap context
     */
    override fun bootstrap(bootstrapContext: BootstrapContext) {
        System.setProperty(
                "java.util.logging.SimpleFormatter.format", $$"[%1$tT] [%4$-7s] %5$s %n"
        );
        bootstrapContext.logger.warn("Bootstrapping JustEnoughPaper...");
    }

    /**
     * create the plugin instance
     * @param pluginProviderContext the plugin provider context
     * @return the plugin instance
     */
    @Override
    override fun createPlugin(pluginProviderContext: PluginProviderContext): JavaPlugin {
        pluginProviderContext.logger.warn("Creating JustEnoughPaper instance...");
        return JustEnoughPaper();
    }
}