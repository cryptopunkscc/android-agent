package cc.cryptopunks.astral.agent.node

import android.content.Context

internal fun Context.createApphostConfig() {
    astralDir.resolve("mod_apphost.yaml").apply {
        if (!exists())
            writeText(modApphostConfig)
    }
}

private val modApphostConfig = """
default_identity: localhost
""".trimIndent()
