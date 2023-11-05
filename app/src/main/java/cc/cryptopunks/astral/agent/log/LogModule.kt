package cc.cryptopunks.astral.agent.log

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val logModule = module {
    singleOf(::LogPreferences)
    viewModelOf(::LogViewModel)
}
