package lol.simeon.jer.protocol.fabric

import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import lol.simeon.jer.protocol.SharedHelper
import lol.simeon.jer.protocol.fabric.packet.RecipeSyncPayload
import lol.simeon.jer.protocol.fabric.recipe.FabricRecipeEntryBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket
import net.minecraft.resources.Identifier
import net.minecraft.server.MinecraftServer

object FabricPacketBuilder : SharedHelper() {

    fun buildAndWriteToContext(ctx: ChannelHandlerContext) {
        val entries = FabricRecipeEntryBuilder.buildRecipeSyncEntries()
        val payload = RecipeSyncPayload(entries)

        val server = MinecraftServer.getServer()
        val registryAccess = server.registryAccess()

        val nettyBuf = Unpooled.buffer()
        val registryFriendlyByteBuf = RegistryFriendlyByteBuf(nettyBuf, registryAccess)
        RecipeSyncPayload.CODEC.encode(registryFriendlyByteBuf, payload)

        val bytes = ByteArray(nettyBuf.readableBytes())
        nettyBuf.readBytes(bytes)
        nettyBuf.release()

        val id = Identifier.fromNamespaceAndPath("fabric", "recipe_sync")
        val discarded = constructDiscardedPayload(id, bytes)
        // Must precede ClientboundUpdateRecipesPacket so JEI initializes with recipes already synced.
        ctx.write(ClientboundCustomPayloadPacket(discarded), ctx.voidPromise())
    }
}