package cc.cryptopunks.astral.agent.node

import android.content.Context

internal fun Context.createApphostConfig() {
    astralDir.resolve("mod_apphost.yaml").apply {
        if (!exists())
            writeText(defaultApphostConfig)
    }
}

private val defaultApphostConfig = """
default_identity: localhost
""".trimIndent()
