package cc.cryptopunks.astral.agent

import android.app.Application
import cc.cryptopunks.astral.agent.exception.exceptionModule
import cc.cryptopunks.astral.agent.config.configModule
import cc.cryptopunks.astral.agent.js.jsAppModule
import cc.cryptopunks.astral.agent.node.astralDir
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class AgentApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AgentApp)
            modules(
                module {
                    single { astralDir }
                },
                exceptionModule,
                jsAppModule,
                configModule
            )
        }
    }
}
