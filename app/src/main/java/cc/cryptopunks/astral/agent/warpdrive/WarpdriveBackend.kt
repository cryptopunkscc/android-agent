package cc.cryptopunks.astral.agent.warpdrive

import android.content.Context
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import cc.cryptopunks.astral.bind.astral.Astral
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import java.io.File
import java.util.concurrent.Executors
import kotlin.time.Duration.Companion.seconds

enum class WarpdriveStatus { Starting, Started, Stopped }

private val executor = Executors.newSingleThreadExecutor()

private val scope = CoroutineScope(SupervisorJob() + executor.asCoroutineDispatcher())

internal val warpdriveStatus = MutableStateFlow(WarpdriveStatus.Stopped)

private var warpdriveJob: Job = Job().apply { complete() }

private val warpdriveDir: File = getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS)
    .resolve("warpdrive").apply { mkdirs() }

private val Context.warpdriveCache get() = cacheDir.resolve("warpdrive").apply { mkdirs() }

fun Context.startWarpdrive() {
    if (!warpdriveJob.isCompleted) return
    warpdriveStatus.value = WarpdriveStatus.Starting
    warpdriveJob = scope.launch {
        try {
            Astral.startWarpdrive(warpdriveCache.absolutePath, warpdriveDir.absolutePath)
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            warpdriveStatus.value = WarpdriveStatus.Stopped
        }
    }
    warpdriveStatus.value = WarpdriveStatus.Started
}

fun stopWarpdrive() = runBlocking {
    val status = withTimeoutOrNull(5.seconds) {
        warpdriveStatus.first { it != WarpdriveStatus.Starting }
    }
    if (status != WarpdriveStatus.Stopped) {
        Astral.stopWarpdrive()
        warpdriveJob.join()
    }
}
