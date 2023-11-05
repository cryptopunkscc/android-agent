package cc.cryptopunks.astral.agent.node

import android.content.Context
import cc.cryptopunks.astral.agent.log.md5
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal fun Context.backupLog(
    date: Date = Date(),
) {
    val dir = cacheDir.logDir

    val log = dir.astralLog
    if (!log.exists()) return

    val previous = dir.listFiles()?.dropLastWhile { it == log }?.lastOrNull()
    if (log.md5() == previous?.md5()) return

    val backupName = format.format(date) + log.name
    val backup = dir.resolve(backupName)

    println("creating log backup: $backupName")
    log.copyTo(backup)
}

private val format = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss:SSS_", Locale.US)

internal fun Context.log(string: String) = runCatching {
    astralDir.logDir.astralLog.appendText(string)
}
