package cc.cryptopunks.astral.agent.config

import android.content.Context
import androidx.lifecycle.ViewModel
import cc.cryptopunks.astral.agent.node.astralDir
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File

class ConfigViewModel(
    context: Context,
) : ViewModel() {

    private val dir = context.astralDir

    private val filesState = MutableStateFlow(emptyList<File>())

    init {
        updateFiles()
    }

    val files = filesState.asStateFlow()

    fun create(name: String) = dir.resolve("$name.yaml").createNewFile().also { updateFiles() }

    fun remove(name: String) = dir.resolve(name).delete().also { updateFiles() }

    private fun updateFiles() {
        filesState.value = dir.listFiles().orEmpty().toList().filterExtension(YamlExtension)
    }
}
