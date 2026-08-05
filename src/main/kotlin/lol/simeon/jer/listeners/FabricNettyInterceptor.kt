package lol.simeon.jer.listeners

import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import io.papermc.paper.network.ChannelInitializeListenerHolder
import lol.simeon.jer.JustEnoughRecipes
import lol.simeon.jer.protocol.fabric.FabricPacketBuilder
import net.kyori.adventure.key.Key
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket

/**
 * Adds a per-connection Netty handler that writes the Fabric recipe sync payload to the channel
 * immediately before every ClientboundUpdateRecipesPacket.
 *
 * This matches the timing of the real Fabric API server-side hook, which injects just before
 * `new ClientboundUpdateRecipesPacket(...)` is constructed in PlayerList.placeNewPlayer().
 * JEI Fabric starts up the moment the client processes ClientboundUpdateRecipesPacket (via mixin),
 * so the sync payload must arrive first for Internal.setClientSyncedRecipes() to be called before
 * JeiStarter.verifyClientRecipes() runs.
 */
@Suppress("UnstableApiUsage")
class FabricNettyInterceptor(private val plugin: JustEnoughRecipes) {

    companion object {
        private val LISTENER_KEY = Key.key("jer", "fabric_recipe_interceptor")
        private const val HANDLER_NAME = "jer:fabric_recipe_interceptor"
    }

    init {
        register()
    }

    private fun register() {
        ChannelInitializeListenerHolder.addListener(LISTENER_KEY) { channel ->
            channel.pipeline().addLast(HANDLER_NAME, object : ChannelDuplexHandler() {
                override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise) {
                    if (msg is ClientboundUpdateRecipesPacket) {
                        try {
                            FabricPacketBuilder.buildAndWriteToContext(ctx)
                        } catch (e: Exception) {
                            plugin.logger.warning("Failed to write Fabric recipe sync: ${e.message}")
                        }
                    }
                    super.write(ctx, msg, promise)
                }
            })
        }
    }
}
