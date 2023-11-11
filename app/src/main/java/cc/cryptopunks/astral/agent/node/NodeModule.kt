package cc.cryptopunks.astral.agent.node

import astral.Astral
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val nodeModule = module {
    factoryOf(Astral::newJsAppHostClient)
}
