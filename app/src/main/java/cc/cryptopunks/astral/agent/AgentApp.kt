package cc.cryptopunks.astral.agent

import android.app.Application
import cc.cryptopunks.astral.agent.admin.adminModule
import cc.cryptopunks.astral.agent.compose.composeModule
import cc.cryptopunks.astral.agent.config.configModule
import cc.cryptopunks.astral.agent.contacts.contactsModule
import cc.cryptopunks.astral.agent.exception.exceptionModule
import cc.cryptopunks.astral.agent.js.jsAppModule
import cc.cryptopunks.astral.agent.log.logModule
import cc.cryptopunks.astral.agent.node.nodeModule
import cc.cryptopunks.astral.agent.warpdrive.WarpdriveStatus.Started
import cc.cryptopunks.astral.agent.warpdrive.warpdriveStatus
import cc.cryptopunks.wrapdrive.WarpdriveStatus
import cc.cryptopunks.wrapdrive.warpdriveModule
import kotlinx.coroutines.flow.map
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class AgentApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AgentApp)
            modules(
                exceptionModule,
                nodeModule,
                logModule,
                configModule,
                adminModule,
                jsAppModule,
                contactsModule,
                composeModule,
                warpdriveModule,
                module {
                    single {
                        WarpdriveStatus(warpdriveStatus.map { status -> status == Started })
                    }
                }
            )
        }
    }
}
