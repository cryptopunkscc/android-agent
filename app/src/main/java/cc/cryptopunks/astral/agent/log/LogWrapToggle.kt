package cc.cryptopunks.astral.agent.log

import androidx.compose.foundation.Image
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun LogWrapToggle(
    preferences: LogPreferences = koinInject(),
) {
    LogWrapToggle(
        enabled = preferences.softWrap,
    ) {
        preferences.softWrap = !preferences.softWrap
    }
}

@Preview
@Composable
private fun LogWrapTogglePreview() {
    LogWrapToggle(
        enabled = true,
    ) {}
}

@Composable
private fun LogWrapToggle(
    enabled: Boolean,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
    ) {
        val res = when (enabled) {
            true -> Icons.Default.Menu
            false -> Icons.Default.Menu
        }
        Image(
            imageVector = res,
            contentDescription = "soft wrap",
            colorFilter = ColorFilter.tint(LocalContentColor.current)
        )
    }
}
