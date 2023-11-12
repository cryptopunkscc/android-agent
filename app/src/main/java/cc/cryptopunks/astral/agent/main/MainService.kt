package cc.cryptopunks.astral.agent.main

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import astral.HandlersWorker
import cc.cryptopunks.astral.agent.admin.AdminClient
import cc.cryptopunks.astral.agent.js.JsAppsManager
import cc.cryptopunks.astral.agent.node.startAstral
import cc.cryptopunks.astral.agent.node.stopAstral
import cc.cryptopunks.astral.agent.warpdrive.startWarpdrive
import cc.cryptopunks.astral.agent.warpdrive.stopWarpdrive
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class AstraldService : Service(), CoroutineScope {

    override val coroutineContext = SupervisorJob() + Dispatchers.IO

    private val jsAppsManager: JsAppsManager by inject()
    private val adminClient: AdminClient by inject()
    private val handlersWorker: HandlersWorker by inject()

    private val tag = javaClass.simpleName

    override fun onCreate() {
        Log.d(tag, "onCreate")

        launch {
            Log.d(tag, "Starting astral service")
            startAstral()
            delay(200)
            handlersWorker.startAsync()
            delay(200)
            startWarpdrive()
            jsAppsManager.startServices()
            adminClient.connect()
        }
    }

    override fun onLowMemory() {
        Log.d(tag, "onLowMemory")
    }

    override fun onTrimMemory(level: Int) {
        Log.d(tag, "onTrimMemory")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.d(tag, "onTaskRemoved")
    }

    override fun onDestroy() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        Log.d(tag, "Destroying astral service")
        jsAppsManager.stopServices()
        stopWarpdrive()
        handlersWorker.cancel()
        stopAstral()
        cancel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(tag, "onStartCommand")
        startForegroundNotification()
        return START_STICKY
    }

    override fun onBind(intent: Intent) = null
}

internal fun Context.startAstralService() {
    val intent = Intent(this, AstraldService::class.java)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startForegroundService(intent)
    } else {
        startService(intent)
    }
}

internal fun Context.stopAstralService() {
    val intent = Intent(this, AstraldService::class.java)
    stopService(intent)
}
