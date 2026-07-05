package lol.simeon.jer

import lol.simeon.testify.junit.TestifyContext
import lol.simeon.testify.junit.TestifyServer
import lol.simeon.testify.junit.shouldBeConnected
import org.junit.jupiter.api.Test

@TestifyServer(
    version = "1.21.11",
    plugins = ["build/libs/JustEnoughRecipes-1.2-SNAPSHOT-obf.jar"],
    helperPlugin = true,
    runOnce = true,
    maxRam = "2G",
)
class JustEnoughRecipesTestifyTest {
    @Test
    fun `plugin starts and accepts a bot`(context: TestifyContext) {
        val bot = context.connectBot("JerSmoke")

        bot.shouldBeConnected()
        bot.disconnect("smoke test complete")
    }
}
