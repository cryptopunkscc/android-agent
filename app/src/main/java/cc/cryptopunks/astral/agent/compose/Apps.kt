package cc.cryptopunks.astral.agent.compose

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cc.cryptopunks.astral.agent.JsApp

@Preview
@Composable
fun AppsPreview() = AstralTheme {
    Apps(
        apps = listOf(
            JsApp("Some example app", ""),
            JsApp("Other example app", ""),
            JsApp("Additional  app for preview", ""),
        )
    )
}

@Composable
fun Apps(
    apps: List<JsApp>,
    installClick: (Uri) -> Unit = {},
    startClick: (JsApp) -> Unit = {},
    uninstallClick: (JsApp) -> Unit = {},
) = Scaffold(
    floatingActionButton = {
        val filePickerLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            uri?.let(installClick)
        }
        FloatingActionButton(
            onClick = {
                filePickerLauncher.launch("application/zip")
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add js application"
            )
        }
    },
) {
    LazyColumn(
        modifier = Modifier.padding(it)
    ) {
        items(apps) { app ->
            Row(
                modifier = Modifier
                    .clickable {
                        startClick(app)
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = app.name)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        uninstallClick(app)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Uninstall app"
                    )
                }
            }
        }
    }
}
