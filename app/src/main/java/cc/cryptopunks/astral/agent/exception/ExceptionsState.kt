package cc.cryptopunks.astral.agent.exception

internal typealias ExceptionsState = MutableList<Exception>

inline infix fun ExceptionsState.catch(block: () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
        this += e
    }
}
