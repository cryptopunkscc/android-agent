package cc.cryptopunks.astral.agent.node

import android.content.Context

internal fun Context.createLogConfig() {
    astralDir.resolve("log.yaml").run {
        if (!exists()) {
            writeText(astralLogConfig())
        }
    }
}

private fun Context.astralLogConfig() = """
    log_file: $astralLog
""".trimIndent()

val Context.astralLog get() = cacheDir.resolve("log").apply { mkdirs() }.resolve("astral.log")
