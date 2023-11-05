package cc.cryptopunks.astral.agent.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainBarToggle(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
    ) {
        Surface(
            border = BorderStroke(2.dp, LocalContentColor.current),
            modifier = Modifier.size(width = 16.dp, height = 20.dp),
            shape = RoundedCornerShape(4.dp),
            content = {},
        )
    }
}
