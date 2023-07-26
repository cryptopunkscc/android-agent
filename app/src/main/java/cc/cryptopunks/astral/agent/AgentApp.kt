package cc.cryptopunks.astral.agent

import android.app.Application

lateinit var jsAppsManager: JsAppsManager private set

class AgentApp : Application() {
    override fun onCreate() {
        super.onCreate()
        jsAppsManager = JsAppsManager(this)
    }
}
