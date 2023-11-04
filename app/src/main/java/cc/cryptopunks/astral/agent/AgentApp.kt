package cc.cryptopunks.astral.agent

import android.app.Application
import cc.cryptopunks.astral.agent.compose.errorsModule
import cc.cryptopunks.astral.agent.config.createApphostConfig
import cc.cryptopunks.astral.agent.js.jsAppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AgentApp : Application() {

    override fun onCreate() {
        super.onCreate()
        createApphostConfig()
        startKoin {
            androidContext(this@AgentApp)
            modules(
                errorsModule,
                jsAppModule,
            )
        }
    }
}
