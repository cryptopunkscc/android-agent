package cc.cryptopunks.astral.agent.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cc.cryptopunks.astral.agent.exception.ExceptionsState
import cc.cryptopunks.astral.agent.exception.catch

class AdminViewModel(
    private val client: AdminClient,
    private val exceptions: ExceptionsState
) : ViewModel() {

    val output get() = client.output

    var query by mutableStateOf("")

    fun query() = exceptions catch {
        client.query(query)
    }
}
