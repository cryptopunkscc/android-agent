package cc.cryptopunks.astral.agent.warpdrive

import android.content.Context
import astral.Astral
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import java.util.concurrent.Executors
import kotlin.time.Duration.Companion.seconds

enum class WarpdriveStatus { Starting, Started, Stopped }

private val executor = Executors.newSingleThreadExecutor()

private val scope = CoroutineScope(SupervisorJob() + executor.asCoroutineDispatcher())

private val status = MutableStateFlow(WarpdriveStatus.Stopped)

private var warpdriveJob: Job = Job().apply { complete() }

val Context.warpdriveDir get() = dataDir.resolve("warpdrive").apply { mkdirs() }

fun Context.startWarpdrive() {
    if (!warpdriveJob.isCompleted) return
    status.value = WarpdriveStatus.Starting
    warpdriveJob = scope.launch {
        try {
            Astral.startWarpdrive(warpdriveDir.absolutePath)
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            status.value = WarpdriveStatus.Stopped
        }
    }
    status.value = WarpdriveStatus.Started
}

fun stopWarpdrive() = runBlocking {
    val status = withTimeoutOrNull(5.seconds) {
        status.first { it != WarpdriveStatus.Starting }
    }
    if (status != WarpdriveStatus.Stopped) {
        Astral.stopWarpdrive()
        warpdriveJob.join()
    }
}
