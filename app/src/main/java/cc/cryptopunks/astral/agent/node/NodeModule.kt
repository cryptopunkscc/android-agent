package cc.cryptopunks.astral.agent.node

import android.content.Context
import astral.Astral
import cc.cryptopunks.astral.agent.node.handler.Handlers
import cc.cryptopunks.astral.agent.node.handler.content.contentResolverMethods
import cc.cryptopunks.astral.agent.node.handler.notification.notificationManagerMethods
import cc.cryptopunks.astral.apphost.gsonSerializer
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val nodeModule = module {
    factoryOf(::AppHostClient)
    factoryOf(Astral::newJsAppHostClient)
    factoryOf(Astral::newHandlersWorker)
    factoryOf(Context::handlers)
    single { gsonSerializer }
}

private fun Context.handlers(): astral.Handlers = Handlers(
    notificationManagerMethods(),
    contentResolverMethods(),
)
