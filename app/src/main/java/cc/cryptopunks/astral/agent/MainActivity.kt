package cc.cryptopunks.astral.agent

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.darkColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    private val askPermissions = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        startAstralService()
    }

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        println("resolved uri: $uri")
        if (uri != null) try {
            jsAppsManager.install(uri)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            PackageManager.PERMISSION_GRANTED != checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
        ) {
            askPermissions.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
        startAstralService()
        setContent {
            AstralTheme {
                Scaffold(
                    floatingActionButton = {
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
                    topBar = {
                        TopAppBar(title = {
                            Text(text = "Astral Agent")
                        })
                    },
                    content = { paddingValues ->
                        val apps by jsAppsManager.apps.collectAsState()
                        LazyColumn(
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            items(apps) { app ->
                                Row(
                                    modifier = Modifier
                                        .clickable { startJsAppActivity(app) }
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(text = app.name)
                                    Spacer(modifier = Modifier.weight(1f))
                                    IconButton(onClick = {
                                        jsAppsManager.uninstall(app.name)
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Uninstall app"
                                        )
                                    }
                                }
                            }
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun AstralTheme(
    content: @Composable () -> Unit,
) = MaterialTheme(
    colors = darkColors(),
    content = content
)
