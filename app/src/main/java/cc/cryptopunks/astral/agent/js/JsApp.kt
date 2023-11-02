package cc.cryptopunks.astral.agent.js

import android.content.Context

internal data class JsApp(
    val name: String,
    val dir: String,
    val description: String = "",
    val icon: String? = null,
    val service: String? = null,
)

internal val Context.appsDir get() = dataDir.resolve("apps")
