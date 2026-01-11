package lol.simeon.jep.protocol.packet

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeSerializer
import kotlin.jvm.optionals.getOrNull

data class RecipeEntry(
    val serializer: RecipeSerializer<*>,
    val recipes: List<RecipeHolder<*>>
) {

    fun write(buf: RegistryFriendlyByteBuf) {
        val serializerKey = BuiltInRegistries.RECIPE_SERIALIZER.getKey(serializer)
        buf.writeUtf(serializerKey.toString())
        buf.writeVarInt(recipes.size)

        val recipeCodec = serializer.streamCodec() as StreamCodec<RegistryFriendlyByteBuf, Recipe<*>>

        for (holder in recipes) {
            buf.writeResourceKey(holder.id())
            recipeCodec.encode(buf, holder.value())
        }
    }

    companion object {
        val CODEC: StreamCodec<RegistryFriendlyByteBuf, RecipeEntry> =
            StreamCodec.ofMember(RecipeEntry::write, ::read)

        private fun read(buf: RegistryFriendlyByteBuf): RecipeEntry {
            val serializerIdStr = buf.readUtf()
            val serializerId = Identifier.parse(serializerIdStr)
            val serializer = BuiltInRegistries.RECIPE_SERIALIZER.get(serializerId)
                .getOrNull()
                ?.value()
                ?: error("Tried syncing unsupported recipe serializer '$serializerId'.")

            val count = buf.readVarInt()
            val list = ArrayList<RecipeHolder<*>>(count)

            repeat(count) {
                val id = buf.readResourceKey(Registries.RECIPE)
                val recipe = (serializer.streamCodec() as StreamCodec<RegistryFriendlyByteBuf, Recipe<*>>).decode(buf)
                list += RecipeHolder(id, recipe)
            }

            return RecipeEntry(serializer, list)
        }
    }

}