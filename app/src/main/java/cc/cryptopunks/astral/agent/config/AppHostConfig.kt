package cc.cryptopunks.astral.agent.config

import android.content.Context
import cc.cryptopunks.astral.agent.node.astralDir


fun Context.createApphostConfig() {
    astralDir.resolve("mod_apphost.yaml").writeText(modApphostConfig)
}

private val modApphostConfig = """
default_identity: localhost    
""".trimIndent()
