package cc.cryptopunks.wrapdrive.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun HorizontalDivider(
    content: @Composable () -> Unit,
) = Column {
    content()
    HorizontalDivider()
}

@Composable
fun HorizontalDivider() = Spacer(
    Modifier
        .background(LocalContentColor.current.copy(0.25f))
        .fillMaxWidth()
        .height(0.5.dp)
)
