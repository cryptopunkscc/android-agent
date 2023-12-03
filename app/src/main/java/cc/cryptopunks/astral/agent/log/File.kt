package cc.cryptopunks.astral.agent.log

import android.os.FileObserver
import cc.cryptopunks.astral.agent.util.fileObserver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.isActive
import java.io.File

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
