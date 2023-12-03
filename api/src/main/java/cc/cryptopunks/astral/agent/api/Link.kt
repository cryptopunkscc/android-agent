package cc.cryptopunks.astral.agent.api

import kotlinx.coroutines.flow.StateFlow

data class Link(
    val id: String,
    val remoteId: String,
    val remote: String,
    val network: String,
    val idle: Long,
    val since: Long,
    val latency: Long,
) {

    interface Repository {
        val flow: StateFlow<List<Link>>
    }
}
