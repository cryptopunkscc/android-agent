package cc.cryptopunks.astral.agent.config

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val configModule = module {
    viewModelOf(::ConfigViewModel)
    viewModelOf(::ConfigEditorViewModel)
}
