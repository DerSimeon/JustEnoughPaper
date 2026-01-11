package lol.simeon.jep.protocol

import io.netty.buffer.Unpooled
import lol.simeon.jep.protocol.packet.RecipeSyncPayload
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket
import net.minecraft.network.protocol.common.custom.DiscardedPayload
import net.minecraft.resources.Identifier
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player

object PacketBuilder {

    fun sendRecipeSyncAsDiscarded(player: Player, payload: RecipeSyncPayload) {
        val serverPlayer = (player as CraftPlayer).handle
        val connection = serverPlayer.connection.connection

        val registryAccess = serverPlayer.registryAccess()
        val nettyBuf = Unpooled.buffer()
        val registryFriendlyByteBuf = RegistryFriendlyByteBuf(nettyBuf, registryAccess)

        // encode your payload into bytes
        RecipeSyncPayload.CODEC.encode(registryFriendlyByteBuf, payload)

        val bytes = ByteArray(nettyBuf.readableBytes())
        nettyBuf.readBytes(bytes)

        val id = Identifier.fromNamespaceAndPath("fabric", "recipe_sync")
        val discarded = DiscardedPayload(id, bytes)

        connection.send(ClientboundCustomPayloadPacket(discarded))
    }
}