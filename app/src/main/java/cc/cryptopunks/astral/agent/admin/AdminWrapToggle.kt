package cc.cryptopunks.astral.agent.admin

import androidx.compose.runtime.Composable
import cc.cryptopunks.astral.agent.compose.SoftWrapToggle
import org.koin.compose.koinInject

@Composable
fun AdminWrapToggle(
    preferences: AdminPreferences = koinInject(),
) = SoftWrapToggle(
    state = preferences.softWrap,
)
