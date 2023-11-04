package cc.cryptopunks.astral.agent.config

import android.os.Build
import android.os.FileObserver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.io.File

internal fun File.flowFiles() = callbackFlow<List<File>> {
    fun filesList() = listFiles()?.toList() ?: emptyList()
    channel.trySend(filesList())
    val observer = fileObserver(FileObserver.CREATE or FileObserver.DELETE) { _, _ ->
        channel.trySend(filesList())
    }
    observer.startWatching()
    awaitClose { observer.stopWatching() }
}.distinctUntilChanged()

internal fun File.fileObserver(
    mask: Int,
    onEvent: (event: Int, path: String?) -> Unit,
): FileObserver = when {

    Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
    -> object : FileObserver(absolutePath, mask) {
        override fun onEvent(event: Int, path: String?) = onEvent(event, path)
    }

    else
    -> object : FileObserver(this, mask) {
        override fun onEvent(event: Int, path: String?) = onEvent(event, path)
    }
}

internal fun Flow<List<File>>.filterExtension(set: Set<String>) = map { it.filterExtension(set) }

internal fun List<File>.filterExtension(set: Set<String>) = filter { it.hasExtension(set) }

internal fun File.hasExtension(set: Set<String>) = extension in set
