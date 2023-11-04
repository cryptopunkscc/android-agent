package cc.cryptopunks.astral.agent.dashboard

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

data class DashboardItem(
    override val label: String,
    override val icon: ImageVector,
    override val content: @Composable () -> Unit,
) : DashboardBottomItem, DashboardScreenItem {

    val route: String = label
}

interface DashboardBottomItem {
    val label: String
    val icon: ImageVector
}

interface DashboardScreenItem {
    val content: @Composable () -> Unit
}


@Preview
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(
        items = remember {
            listOf(
                DashboardItem("config", Icons.Default.Settings) {},
                DashboardItem("apps", Icons.Default.PlayArrow) {},
            )
        }
    )
}


@Composable
fun DashboardScreen(
    navController: NavHostController = rememberNavController(),
    items: List<DashboardItem>,
    actions: @Composable RowScope.() -> Unit = { },
) {

    val onSelect = { item: DashboardItem ->
        val currentRoute = navController.currentDestination?.route
        if (currentRoute != null)
            navController.navigate(item.route) {
                popUpTo(currentRoute) {
                    inclusive = true
                }
            }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Astral Agent") },
                actions = actions,
            )
        },
        content = { paddingValues ->
            NavHost(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                startDestination = items.first().route,
            ) {
                items.forEach { item ->
                    composable(item.route) {
                        item.content()
                    }
                }
            }
        },
        bottomBar = {
            var selected by remember { mutableStateOf(items.first()) }
            BottomNavigation {
                items.forEach { item ->
                    BottomNavigationItem(
                        selected = selected == item,
                        onClick = {
                            selected = item
                            onSelect(item)
                        },
                        icon = { Icon(imageVector = item.icon, contentDescription = "") },
                        label = { Text(text = item.label) }
                    )
                }
            }
        }
    )
}
