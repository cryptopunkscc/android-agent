package cc.cryptopunks.astral.agent.config

import android.os.FileObserver
import cc.cryptopunks.astral.agent.util.fileObserver
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

internal fun Flow<List<File>>.filterExtension(set: Set<String>) = map { it.filterExtension(set) }

internal fun List<File>.filterExtension(set: Set<String>) = filter { it.hasExtension(set) }

internal fun File.hasExtension(set: Set<String>) = extension in set
