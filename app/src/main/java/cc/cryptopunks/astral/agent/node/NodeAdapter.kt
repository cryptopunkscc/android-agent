package cc.cryptopunks.astral.agent.node

import android.content.Context
import android.util.Log
import cc.cryptopunks.astral.bind.astral.Astral
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import java.util.concurrent.Executors
import kotlin.time.Duration.Companion.seconds

enum class AstralStatus { Starting, Started, Stopped }

private val executor = Executors.newSingleThreadExecutor()

private val scope = CoroutineScope(SupervisorJob() + executor.asCoroutineDispatcher())

private val status = MutableStateFlow(AstralStatus.Stopped)

private val identity = CompletableDeferred<String>()

private var astralJob: Job = Job().apply { complete() }

/**
 * A time when the node was started.
 */
var startTime: Long = 0; private set

/**
 * Current [AstralStatus].
 */
val astralStatus: StateFlow<AstralStatus> get() = status

/**
 * Astrald main directory.
 */
val Context.astralDir get() = dataDir.resolve("astrald").apply { mkdirs() }

/**
 * Local node identity.
 */
suspend fun astralIdentity() = identity.await()

/**
 * Start astrald in background.
 */
fun Context.startAstral() {
    if (status.value == AstralStatus.Stopped) {
        startTime = System.currentTimeMillis()
        status.value = AstralStatus.Starting

        // Start astral daemon
        astralJob = scope.launch {
            val multicastLock = acquireMulticastWakeLock()
            try {
                backupLog()
                createApphostConfig()
                createLogConfig()
                Astral.start(astralDir.absolutePath)
            } catch (e: Throwable) {
                e.printStackTrace()
                log(e.stackTraceToString())
            } finally {
                status.value = AstralStatus.Stopped
                Log.d("AstralNetwork", "releasing multicast")
                multicastLock.release()
                backupLog()
            }
        }

        // Resolve local node id
        runBlocking {
            val id = flow { while (true) emit(delay(10)) }
                .map { Astral.identity() }
                .first { id -> !id.isNullOrBlank() }
            Log.d("AstralNetwork", id)
            identity.complete(id)
        }

        status.value = AstralStatus.Started
    }
}

/**
 * Stop astrald if running.
 */
fun stopAstral() = runBlocking {
    val status = withTimeoutOrNull(5.seconds) {
        status.first { it != AstralStatus.Starting }
    }
    if (status != AstralStatus.Stopped) {
        Astral.stop()
        astralJob.join()
    }
}
