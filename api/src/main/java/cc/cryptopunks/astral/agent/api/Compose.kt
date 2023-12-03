package cc.cryptopunks.astral.agent.api

import androidx.compose.runtime.Composable
import org.koin.compose.koinInject

val ext @Composable get() = koinInject<ComposeApi>()

@Suppress("PropertyName")
data class ComposeApi(
    val Theme: @Composable (@Composable () -> Unit) -> Unit,
    val Contacts: @Composable ((String) -> Unit) -> Unit,
    val Errors: @Composable (MutableList<Exception>) -> Unit,
)
