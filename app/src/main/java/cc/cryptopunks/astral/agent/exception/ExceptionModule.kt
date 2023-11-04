package cc.cryptopunks.astral.agent.exception

import androidx.compose.runtime.mutableStateListOf
import org.koin.dsl.module

val exceptionModule = module {
    single<ExceptionsState> { mutableStateListOf() }
}
