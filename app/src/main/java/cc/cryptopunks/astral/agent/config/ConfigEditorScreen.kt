package cc.cryptopunks.astral.agent.config

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.navigation.koinNavViewModel

@Composable
fun ConfigEditorScreen(
    navController: NavController,
    model: ConfigEditorViewModel = koinNavViewModel(),
) {
    ConfigEditorScreen(
        title = model.file.name,
        text = model.text,
        onTextChange = { model.text = it },
        save = model::save,
        close = navController::popBackStack
    )
}

@Preview
@Composable
fun ConfigEditorScreenPreview() {
    val text = remember { mutableStateOf("") }
    ConfigEditorScreen(
        title = "test_config.yaml",
        text = text.value,
        onTextChange = text::value::set,
    )
}

@Composable
fun ConfigEditorScreen(
    title: String,
    text: String,
    onTextChange: (String) -> Unit,
    save: () -> Unit = {},
    close: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(onClick = {
                        save()
                        close()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "save"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = close) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "close",
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(0.dp),
            singleLine = false,
        )
    }
}
