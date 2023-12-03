package cc.cryptopunks.wrapdrive.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cc.cryptopunks.astral.agent.api.startAgentActivity
import cc.cryptopunks.astral.agent.api.startAgentService
import cc.cryptopunks.wrapdrive.WarpdriveStatus
import org.koin.compose.koinInject

@Composable
fun WarpdriveConnectionView(
    modifier: Modifier = Modifier,
    warpdriveStatus: WarpdriveStatus = koinInject(),
    content: @Composable () -> Unit,
) = Box(modifier = modifier) {
    val context = LocalContext.current
    val isConnected by warpdriveStatus.collectAsState(true)
    if (isConnected) content()
    else DisconnectionView(
        startAstralActivity = context::startAgentActivity,
        startAstralService = context::startAgentService,
    )
}

@Preview
@Composable
private fun DisconnectionPreview() = PreviewBox {
    DisconnectionView()
}

@Composable
private fun DisconnectionView(
    startAstralService: () -> Unit = {},
    startAstralActivity: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Cannot connect to astral network",
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(48.dp))
        Text(
            text = "Make sure the local astral service is running",
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = { startAstralService() }
        ) {
            Text(text = "start astral service".uppercase())
        }
        Text(text = "or")
        Button(
            onClick = { startAstralActivity() }
        ) {
            Text(text = "start astral activity".uppercase())
        }
    }
}
