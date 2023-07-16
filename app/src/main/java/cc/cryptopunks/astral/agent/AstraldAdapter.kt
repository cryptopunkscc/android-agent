package cc.cryptopunks.astral.agent

import android.content.Context
import android.util.Log
import astral.Astral
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
import java.io.File
import java.util.concurrent.Executors
import kotlin.time.Duration.Companion.seconds

const val ASTRAL = "Astral"

private val executor = Executors.newSingleThreadExecutor()

private val scope = CoroutineScope(SupervisorJob() + executor.asCoroutineDispatcher())

private var astralJob: Job = Job().apply { complete() }

private val identity = CompletableDeferred<String>()

private val status = MutableStateFlow(AstralStatus.Stopped)

val Context.astralDir get() = File(applicationInfo.dataDir)

var startTime: Long = 0; private set

val astralStatus: StateFlow<AstralStatus> get() = status

enum class AstralStatus { Starting, Started, Stopped }

fun Context.startAstral() {
    if (status.value == AstralStatus.Stopped) {
        startTime = System.currentTimeMillis()
        status.value = AstralStatus.Starting

        // Start astral daemon
        astralJob = scope.launch {
            val multicastLock = acquireMulticastWakeLock()
            try {
                createApphostConfig()
                val dir = astralDir.absolutePath
//                val handlers = Handlers.from(resolveMethods())
//                val bluetooth = Bluetooth()
                Astral.start(
                    dir,
//                    handlers,
//                    bluetooth,
                )
            } catch (e: Throwable) {
                e.printStackTrace()
            } finally {
                status.value = AstralStatus.Stopped
                Log.d("AstralNetwork", "releasing multicast")
                multicastLock.release()
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

fun stopAstral() = runBlocking {
    val status = withTimeoutOrNull(5.seconds) {
        status.first { it != AstralStatus.Starting }
    }
    if (status != AstralStatus.Stopped) {
        Astral.stop()
        astralJob.join()
    }
}

suspend fun astralIdentity() = identity.await()

fun Context.createApphostConfig() {
    astralDir.resolve("astrald").apply {
        mkdirs()
        resolve("mod_apphost.yaml").writeText(modApphostConfig)
    }
}

private val modApphostConfig = """
allow_anonymous: true    
""".trimIndent()
