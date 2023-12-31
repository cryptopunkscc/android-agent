package cc.cryptopunks.astral.agent.log

import androidx.compose.runtime.Composable
import cc.cryptopunks.astral.agent.compose.SoftWrapToggle
import org.koin.compose.koinInject

@Composable
fun LogWrapToggle(
    preferences: LogPreferences = koinInject(),
) = SoftWrapToggle(
    preferences.softWrap,
)
