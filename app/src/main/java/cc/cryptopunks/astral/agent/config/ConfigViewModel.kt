package cc.cryptopunks.astral.agent.config

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.cryptopunks.astral.agent.node.astralDir
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ConfigViewModel(
    context: Context,
) : ViewModel() {

    private val dir = context.astralDir

    val files = dir.flowFiles()
        .filterExtension(YamlExtension)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun create(name: String) = dir.resolve("$name.yaml").createNewFile()

    fun remove(name: String) = dir.resolve(name).delete()
}
