package cc.cryptopunks.astral.agent.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.io.File

class ConfigViewModel(
    private val dir: File,
) : ViewModel() {

    val files = dir.flowFiles()
        .filterExtension(YamlExtension)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun create(name: String) = dir.resolve("$name.yaml").createNewFile()

    fun remove(name: String) = dir.resolve(name).delete()
}
