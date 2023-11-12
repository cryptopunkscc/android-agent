package cc.cryptopunks.astral.agent.contacts

data class Link(
    val id: String,
    val remoteId: String,
    val remote: String,
    val network: String,
    val idle: Long,
    val since: Long,
    val latency: Long,
)

