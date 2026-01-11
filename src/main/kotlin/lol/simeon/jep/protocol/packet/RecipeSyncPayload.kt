package lol.simeon.jep.protocol.packet

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

class RecipeSyncPayload(val entries: List<RecipeEntry>) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = ID

    companion object {
        val ID: CustomPacketPayload.Type<RecipeSyncPayload> =
            CustomPacketPayload.Type(Identifier.fromNamespaceAndPath("fabric", "recipe_sync"))

        val CODEC: StreamCodec<RegistryFriendlyByteBuf, RecipeSyncPayload> =
            RecipeEntry.CODEC
                .apply(ByteBufCodecs.list())
                .map(::RecipeSyncPayload, RecipeSyncPayload::entries)
    }
}