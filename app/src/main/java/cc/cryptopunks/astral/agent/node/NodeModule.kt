package cc.cryptopunks.astral.agent.node

import android.content.Context
import cc.cryptopunks.astral.agent.node.handler.Handlers
import cc.cryptopunks.astral.agent.node.handler.content.ContentResolverFactory
import cc.cryptopunks.astral.agent.node.handler.content.contentResolverMethods
import cc.cryptopunks.astral.agent.node.handler.notification.Notifier
import cc.cryptopunks.astral.agent.node.handler.notification.notificationManagerMethods
import cc.cryptopunks.astral.apphost.GsonAppHostClient
import cc.cryptopunks.astral.apphost.defaultGson
import cc.cryptopunks.astral.apphost.gsonSerializer
import cc.cryptopunks.astral.bind.android.NotifyServiceApi
import cc.cryptopunks.astral.bind.astral.Astral
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import cc.cryptopunks.astral.bind.astral.Handlers as AstralHandlers

val nodeModule = module {
    single { defaultGson }
    single { gsonSerializer }
    single { CoroutineScope(SupervisorJob() + Dispatchers.IO) }
    factoryOf(::AppHostClient)
    factoryOf(::GsonAppHostClient)
    singleOf(::Notifier).bind<NotifyServiceApi>()
    factoryOf(::ContentResolverFactory)
    factoryOf(Astral::newJsAppHostClient)
    factoryOf(Astral::newHandlersWorker)
    factoryOf(Context::handlers)
}

private fun Context.handlers(): AstralHandlers = Handlers(
    notificationManagerMethods(),
    contentResolverMethods(),
)
