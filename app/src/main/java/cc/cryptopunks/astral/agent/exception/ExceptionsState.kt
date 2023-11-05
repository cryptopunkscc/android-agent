package cc.cryptopunks.astral.agent.exception

internal typealias ExceptionsState = MutableList<Throwable>

inline infix fun ExceptionsState.catch(block: () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
        this += e
    }
}
