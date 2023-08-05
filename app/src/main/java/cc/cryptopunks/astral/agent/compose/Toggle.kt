package cc.cryptopunks.astral.agent.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import cc.cryptopunks.astral.agent.AstralStatus
import cc.cryptopunks.astral.agent.R
import cc.cryptopunks.astral.agent.astralStatus
import cc.cryptopunks.astral.agent.startAstralService
import cc.cryptopunks.astral.agent.stopAstralService


@Composable
fun AstralToggle() {
    val status by astralStatus.collectAsState()
    val context = LocalContext.current
    AstralToggle(status) {
        when (status) {
            AstralStatus.Stopped -> context.startAstralService()
            AstralStatus.Started -> context.stopAstralService()
            AstralStatus.Starting -> Unit
        }
    }
}

@Preview
@Composable
fun AstralTogglePreview() {
    var status by remember { mutableStateOf(AstralStatus.Starting) }
    AstralToggle(
        status = status,
        enabled = true,
    ) {
        val values = AstralStatus.values()
        val next = (status.ordinal + 1) % values.size
        status = values[next]
    }
}

@Composable
fun AstralToggle(
    status: AstralStatus,
    enabled: Boolean = status != AstralStatus.Starting,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
    ) {
        val res = when (status) {
            AstralStatus.Starting -> R.drawable.sync_black_24dp
            AstralStatus.Started -> R.drawable.link_black_24dp
            AstralStatus.Stopped -> R.drawable.link_off_black_24dp
        }
        var angle by remember(status) { mutableStateOf(0f) }
        val rotation = remember(status) { Animatable(angle) }
        LaunchedEffect(status) {
            if (status == AstralStatus.Starting) {
                rotation.animateTo(
                    targetValue = angle - 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(920, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                ) {
                    angle = value
                }
            }
        }
        Image(
            painter = painterResource(res),
            contentDescription = status.name,
            modifier = Modifier.rotate(rotation.value),
            colorFilter = ColorFilter.tint(LocalContentColor.current)
        )
    }
}
