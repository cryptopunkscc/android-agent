package cc.cryptopunks.astral.agent.logcat

import android.content.Context
import cc.cryptopunks.astral.agent.util.createBackup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LogcatBackup(
    private val context: Context,
    private val scope: CoroutineScope,
) {
    fun start() {
        val file = context.logcatLog
        file.createBackup()
        val process = Runtime.getRuntime().exec("logcat")
        scope.launch {
            process.inputStream.use { input ->
                file.outputStream().use {  output ->
                    input.copyTo(output)
                }
            }
        }
    }
}

private val Context.logcatDir get() = cacheDir.resolve("logcat").apply { mkdirs() }
private val Context.logcatLog get() = logcatDir.resolve("logcat.log")
