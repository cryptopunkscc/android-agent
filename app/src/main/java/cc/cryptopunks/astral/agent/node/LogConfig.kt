package cc.cryptopunks.astral.agent.node

import android.content.Context
import java.io.File

internal fun Context.createLogConfig() {
    astralDir.resolve("log.yaml").run {
        if (!exists()) {
            writeText(defaultLogConfig(cacheDir.logDir))
        }
    }
}

private fun defaultLogConfig(dir: File) = """
    log_file: ${dir.astralLog}
""".trimIndent()

val File.astralLog get() = resolve("astral.log")

val File.logDir get() = resolve("log").apply { mkdirs() }
