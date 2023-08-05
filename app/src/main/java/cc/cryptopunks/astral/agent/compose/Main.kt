package cc.cryptopunks.astral.agent.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import cc.cryptopunks.astral.agent.JsAppsManager
import cc.cryptopunks.astral.agent.startJsAppActivity

class MainModel : ViewModel() {
    val errors = mutableStateListOf<Throwable>()
}

@Composable
fun Main(
    model: MainModel,
    jsAppsManager: JsAppsManager,
) {
    AstralTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Astral Agent") },
                    actions = { AstralToggle() }
                )
            },
            content = { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    val context = LocalContext.current
                    val apps by jsAppsManager.apps.collectAsState()
                    Apps(
                        apps = apps,
                        startClick = {
                            try {
                                context.startJsAppActivity(it)
                            } catch (e: Throwable) {
                                e.printStackTrace()
                                model.errors += e
                            }
                        },
                        installClick = { uri ->
                            try {
                                jsAppsManager.install(uri)
                            } catch (e: Throwable) {
                                e.printStackTrace()
                                model.errors += e
                            }
                        },
                        uninstallClick = {
                            try {
                                jsAppsManager.uninstall(it.name)
                            } catch (e: Throwable) {
                                e.printStackTrace()
                                model.errors += e
                            }
                        }
                    )
                    Errors(model.errors)
                }
            },
        )
    }
}
