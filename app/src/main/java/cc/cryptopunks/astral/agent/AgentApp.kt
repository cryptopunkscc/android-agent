package cc.cryptopunks.astral.agent

import android.app.Application
import cc.cryptopunks.astral.agent.config.createApphostConfig
import cc.cryptopunks.astral.agent.js.JsAppsManager

class AgentApp :
    JsAppsManager.Provider,
    Application() {

    override val jsAppsManager by lazy { JsAppsManager(this) }

    override fun onCreate() {
        super.onCreate()
        createApphostConfig()
    }
}
