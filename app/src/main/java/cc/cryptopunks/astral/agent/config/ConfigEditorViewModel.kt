package cc.cryptopunks.astral.agent.config

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File

class ConfigEditorViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val fileName: String = requireNotNull(savedStateHandle["file"])

    val file = File(fileName)

    var text by mutableStateOf(file.readText())

    fun save() = viewModelScope.launch {
        file.writeText(text)
    }
}
