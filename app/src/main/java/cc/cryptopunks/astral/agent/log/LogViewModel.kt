package cc.cryptopunks.astral.agent.log

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.cryptopunks.astral.agent.node.AstralStatus
import cc.cryptopunks.astral.agent.node.astralLog
import cc.cryptopunks.astral.agent.node.astralStatus
import cc.cryptopunks.astral.agent.node.logDir
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn

class LogViewModel(
    context: Context,
) : ViewModel() {

    private val logFile = context.cacheDir.logDir.astralLog

    @OptIn(ExperimentalCoroutinesApi::class)
    val logText: StateFlow<String> = astralStatus
        .filter { status -> status != AstralStatus.Starting }
        .flatMapLatest {
            delay(100)
            logFile.observe()
                .map(::String)
                .scan("") { log, append -> log + append }
        }.stateIn(viewModelScope, SharingStarted.Lazily, "")
}
