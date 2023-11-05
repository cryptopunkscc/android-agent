package cc.cryptopunks.astral.agent

import android.app.Application
import cc.cryptopunks.astral.agent.config.configModule
import cc.cryptopunks.astral.agent.exception.exceptionModule
import cc.cryptopunks.astral.agent.js.jsAppModule
import cc.cryptopunks.astral.agent.log.logModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AgentApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AgentApp)
            modules(
                exceptionModule,
                logModule,
                jsAppModule,
                configModule
            )
        }
    }
}
