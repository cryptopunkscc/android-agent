package cc.cryptopunks.astral.agent.main

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cc.cryptopunks.astral.agent.admin.AdminScreen
import cc.cryptopunks.astral.agent.admin.AdminWrapToggle
import cc.cryptopunks.astral.agent.compose.AstralTheme
import cc.cryptopunks.astral.agent.config.ConfigEditorScreen
import cc.cryptopunks.astral.agent.config.ConfigScreen
import cc.cryptopunks.astral.agent.contacts.ContactsScreen
import cc.cryptopunks.astral.agent.dashboard.DashboardItem
import cc.cryptopunks.astral.agent.dashboard.DashboardScreen
import cc.cryptopunks.astral.agent.js.JsAppsScreen
import cc.cryptopunks.astral.agent.log.LogScreen
import cc.cryptopunks.astral.agent.log.LogWrapToggle

@Composable
fun MainScreen() {
    AstralTheme {
        val mainNavController = rememberNavController()
        val dashboardNavController = rememberNavController()
        var showBars by remember { mutableStateOf(true) }
        NavHost(
            navController = mainNavController, startDestination = "dashboard"
        ) {
            composable("dashboard") {
                DashboardScreen(
                    navController = dashboardNavController,
                    actions = { MainServiceToggle() },
                    showBars = showBars,
                    items = remember {
                        listOf(
                            DashboardItem("log", Icons.Default.List,
                                actions = { LogWrapToggle() }
                            ) {
                                LogScreen()
                            },
                            DashboardItem("config", Icons.Default.Settings) {
                                ConfigScreen { file ->
                                    val route = "config_editor/${Uri.encode(file.absolutePath)}"
                                    mainNavController.navigate(route)
                                }
                            },
                            DashboardItem("admin", Icons.Default.Face,
                                actions = {
                                    MainBarToggle { showBars = false }
                                    AdminWrapToggle()
                                }
                            ) {
                                AdminScreen()
                            },
                            DashboardItem("apps", Icons.Default.PlayArrow) {
                                JsAppsScreen()
                            },
                            DashboardItem("contacts", Icons.Default.Person) {
                                ContactsScreen()
                            },
                        )
                    },
                )
                BackHandler(!showBars) {
                    showBars = true
                }
            }
            composable("config_editor/{file}") {
                ConfigEditorScreen(mainNavController)
            }
        }
    }
}
