package cc.cryptopunks.astral.agent

import android.app.Application
import cc.cryptopunks.astral.agent.admin.adminModule
import cc.cryptopunks.astral.agent.compose.composeModule
import cc.cryptopunks.astral.agent.config.configModule
import cc.cryptopunks.astral.agent.contacts.contactsModule
import cc.cryptopunks.astral.agent.exception.exceptionModule
import cc.cryptopunks.astral.agent.js.jsAppModule
import cc.cryptopunks.astral.agent.log.logModule
import cc.cryptopunks.astral.agent.logcat.logcatModule
import cc.cryptopunks.astral.agent.main.mainModule
import cc.cryptopunks.astral.agent.node.nodeModule
import cc.cryptopunks.astral.agent.logcat.LogcatBackup
import cc.cryptopunks.wrapdrive.warpdriveModule
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.lang.Thread.setDefaultUncaughtExceptionHandler

class AgentApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(
                logcatModule,
                exceptionModule,
                nodeModule,
                mainModule,
                logModule,
                configModule,
                adminModule,
                jsAppModule,
                contactsModule,
                composeModule,
                warpdriveModule,
            )
        }
        setDefaultUncaughtExceptionHandler(get())
        get<LogcatBackup>().start()
    }
}
