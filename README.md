## What JustEnoughPaper does

JustEnoughPaper makes sure that Fabric mods like Just Enough Items (JEI) keep
working when you play on a Paper server.

Starting with Minecraft 1.21.2, recipe syncing changed in a way that prevents JEI to work in a non-modded environment. Because of that, Fabric clients no longer receive the recipe
data they expect, and mods such as JEI can stop showing recipes or behave
incorrectly.

When a player joins the server, JustEnoughPaper collects all crafting recipes
from the server and sends them to the client in the format Fabric mods expect.
This happens automatically and does not change gameplay in any way.

In simple terms:

- Paper has the recipes
- Fabric mods need those recipes
- JustEnoughPaper sends them from the server to the client

This allows mods like Just Enough Items to continue working on Paper servers
after Minecraft 1.21.2, without switching to a Fabric server or installing extra
dependencies.
