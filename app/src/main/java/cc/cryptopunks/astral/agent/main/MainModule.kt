package cc.cryptopunks.astral.agent.main

import cc.cryptopunks.astral.agent.api.ServiceStatus
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val mainModule = module {
    singleOf(::MainServiceStatus).bind<ServiceStatus>()
    singleOf(::MainPermissions)
}
