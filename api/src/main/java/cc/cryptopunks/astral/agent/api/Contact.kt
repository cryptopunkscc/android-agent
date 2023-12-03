package cc.cryptopunks.astral.agent.api

import kotlinx.coroutines.flow.StateFlow

data class Contact(
    val id: String = "",
    val alias: String = "",
) {
    interface Repository {
        val flow: StateFlow<List<Contact>>
    }
}
