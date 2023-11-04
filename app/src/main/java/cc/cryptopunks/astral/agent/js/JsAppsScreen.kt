package cc.cryptopunks.astral.agent.js

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cc.cryptopunks.astral.agent.compose.EditableItemList
import cc.cryptopunks.astral.agent.exception.catch
import org.koin.compose.koinInject

@Composable
fun JsAppsScreen(
    modifier: Modifier = Modifier,
    errors: MutableList<Throwable> = koinInject(),
    jsAppsManager: JsAppsManager = koinInject(),
) {
    Box(modifier = modifier) {
        val context = LocalContext.current
        val apps by jsAppsManager.apps.collectAsState()
        JsApps(
            apps = apps,
            startClick = {
                errors catch  {
                    context.startJsAppActivity(it)
                }
            },
            installClick = { uri ->
                errors catch {
                    jsAppsManager.install(uri)
                }
            },
            uninstallClick = {
                errors catch {
                    jsAppsManager.uninstall(it.name)
                }
            }
        )
    }
}

@Preview
@Composable
private fun JsAppsPreview() {
    JsApps(
        apps = listOf(
            JsApp("Some example app", "", description = "Some example description"),
            JsApp("Other example app", "", description = "Some example description"),
            JsApp("Additional  app for preview", ""),
        )
    )
}

@Composable
private fun JsApps(
    apps: List<JsApp>,
    installClick: (Uri) -> Unit = {},
    startClick: (JsApp) -> Unit = {},
    uninstallClick: (JsApp) -> Unit = {},
) {
    val filePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let(installClick)
    }
    EditableItemList(
        items = apps,
        onAddClick = { filePickerLauncher.launch("application/zip") },
        onRemoveClick = uninstallClick,
        onSelectClick = startClick
    ) { app ->
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
    }
}
