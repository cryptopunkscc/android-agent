package cc.cryptopunks.astral.agent.logcat

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val logcatModule = module {
    singleOf(::LogcatBackup)
}
