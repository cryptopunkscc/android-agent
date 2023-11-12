package cc.cryptopunks.astral.agent.node.handler.content

internal object Content {
    const val read = "android/content"
    const val info = "android/content/info"

    data class Info(
        val uri: String,
        val size: Long,
        val name: String = "",
        val mime: String = "",
    )
}
