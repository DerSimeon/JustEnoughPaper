package lol.simeon.jep.protocol

import lol.simeon.jep.protocol.packet.RecipeEntry
import net.minecraft.server.MinecraftServer
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeSerializer
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.CraftServer

object RecipeEntryBuilder {

    fun buildRecipeSyncEntries(): List<RecipeEntry> {
        val craftServer = Bukkit.getServer() as CraftServer
        val mcServer: MinecraftServer = craftServer.server

        val recipeManager = mcServer.recipeManager

        val all: Collection<RecipeHolder<*>> = recipeManager.recipes.values()

        val grouped: Map<RecipeSerializer<*>, List<RecipeHolder<*>>> = all.groupBy { holder ->
            val recipe = holder.value()
            recipe.serializer
        }

        return grouped.entries.map { (serializer, holders) -> RecipeEntry(serializer, holders) }
            .sortedBy { entry -> entry.serializer.toString() }
    }
}