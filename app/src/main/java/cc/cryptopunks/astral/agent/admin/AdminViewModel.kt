package cc.cryptopunks.astral.agent.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AdminViewModel(
    private val client: AdminClient,
) : ViewModel() {

    val output get() = client.output

    var query by mutableStateOf("")

    fun query() = viewModelScope.launch {
        client.query(query)
    }
}
