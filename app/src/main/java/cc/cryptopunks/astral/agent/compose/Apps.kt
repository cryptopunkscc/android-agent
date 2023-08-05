package cc.cryptopunks.astral.agent.compose

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
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
            JsApp("Some example app", "", description = "Some example description"),
            JsApp("Other example app", "", description = "Some example description"),
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
            Column {
                Row(
                    modifier = Modifier
                        .clickable {
                            startClick(app)
                        }
                        .padding(vertical = 16.dp)
                        .padding(start = 24.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            text = app.name,
                            style = MaterialTheme.typography.subtitle1,
                        )
                        if (app.description.isNotBlank()) {
                            Spacer(modifier = Modifier.size(4.dp))
                            Text(
                                text = app.description,
                                style = MaterialTheme.typography.caption,
                            )
                        }
                    }
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.5.dp)
                        .background(LocalContentColor.current.copy(alpha = LocalContentAlpha.current))
                )
            }
        }
    }
}
