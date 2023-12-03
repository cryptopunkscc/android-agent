package cc.cryptopunks.astral.agent.config

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cc.cryptopunks.astral.agent.compose.AstralTheme
import cc.cryptopunks.astral.agent.compose.EditableItemList
import org.koin.androidx.compose.navigation.koinNavViewModel
import java.io.File

@Composable
fun ConfigScreen(
    model: ConfigViewModel = koinNavViewModel(),
    edit: (File) -> Unit,
) {
    ConfigScreen(
        items = model.files.collectAsStateWithLifecycle().value,
        remove = model::remove,
        create = model::create,
        select = edit,
    )
}

@Composable
private fun ConfigScreen(
    items: List<File>,
    remove: (String) -> Unit,
    create: (String) -> Unit,
    select: (File) -> Unit,
) {
    val showCreateFileDialog = remember { mutableStateOf(false) }
    EditableItemList(
        items = items,
        onRemoveClick = { remove(it.name) },
        onAddClick = { showCreateFileDialog.value = true },
        onSelectClick = select,
    ) {
        Text(text = it.name)
    }

    if (showCreateFileDialog.value)
        CreateFileDialog(
            show = showCreateFileDialog,
            create = create
        )
}

@Preview
@Composable
private fun CreateFileDialogPreview() = AstralTheme {
    CreateFileDialog(
        show = remember { mutableStateOf(true) },
        create = {},
    )
}

@Composable
private fun CreateFileDialog(
    show: MutableState<Boolean>,
    create: (String) -> Unit,
) {
    var fileName by remember { mutableStateOf("") }
    val dismiss = { show.value = false }
    if (show.value) AlertDialog(
        title = { Text(
            text = "Create config",
            modifier = Modifier.padding(bottom = 32.dp)
        ) },
        text = {
            Column {
                Text(text = "")
                OutlinedTextField(
                    value = fileName,
                    onValueChange = { fileName = it },
                    trailingIcon = {
                        Text(
                            text = ".yaml"
                        )
                    },
                    placeholder = { Text(text = "type config name") }
                )
            }
        },
        backgroundColor = Color.DarkGray,
        onDismissRequest = dismiss,
        confirmButton = {
            Button(
                onClick = {
                    dismiss()
                    create(fileName)
                },
                enabled = fileName.isNotEmpty()
            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(onClick = dismiss) {
                Text(text = "Cancel")
            }
        },
    )
}
