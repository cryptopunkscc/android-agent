package cc.cryptopunks.astral.agent.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cc.cryptopunks.astral.agent.compose.AstralTheme
import cc.cryptopunks.astral.agent.compose.Errors
import cc.cryptopunks.astral.agent.js.JsAppsScreen

@Composable
fun MainScreen() {
    AstralTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Astral Agent") },
                    actions = { MainServiceToggle() }
                )
            },
            content = { paddingValues ->
                JsAppsScreen(
                    modifier = Modifier.padding(paddingValues),
                )
            },
        )
        Errors()
    }
}
