package cc.cryptopunks.astral.agent.node

import android.content.Context
import cc.cryptopunks.astral.agent.util.FileDateFormat
import cc.cryptopunks.astral.agent.util.md5
import java.util.Date

internal fun Context.backupLog(
    date: Date = Date(),
) {
    val dir = cacheDir.logDir

    val log = dir.astralLog
    if (!log.exists()) return

    val previous = dir.listFiles()?.dropLastWhile { it == log }?.lastOrNull()
    if (log.md5() == previous?.md5()) return

    val backupName = FileDateFormat.format(date) + log.name
    val backup = dir.resolve(backupName)

    println("creating log backup: $backupName")
    log.copyTo(backup)
}

internal fun Context.log(string: String) = runCatching {
    astralDir.logDir.astralLog.appendText(string)
}
