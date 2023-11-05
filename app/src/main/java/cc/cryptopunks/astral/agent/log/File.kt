package cc.cryptopunks.astral.agent.log

import android.os.FileObserver
import cc.cryptopunks.astral.agent.util.fileObserver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.isActive
import java.io.File
import java.security.MessageDigest

internal fun File.observe(): Flow<ByteArray> = channelFlow {
    val inputStream = inputStream()
    channel.trySend(inputStream.readBytes())
    val observer = fileObserver(FileObserver.MODIFY) { _, _ ->
        if (isActive) {
            val arr = inputStream.readBytes()
            channel.trySend(arr)
        }
    }
    observer.startWatching()
    awaitClose {
        inputStream.close()
        observer.stopWatching()
    }
}

internal fun File.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return this.inputStream().use { fis ->
        val buffer = ByteArray(8192)
        generateSequence {
            when (val bytesRead = fis.read(buffer)) {
                -1 -> null
                else -> bytesRead
            }
        }.forEach { bytesRead -> md.update(buffer, 0, bytesRead) }
        md.digest().joinToString("") { "%02x".format(it) }
    }
}
