package cc.cryptopunks.astral.agent.js

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val jsAppModule = module {
    singleOf(::JsAppsManager)
}
