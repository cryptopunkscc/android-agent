package cc.cryptopunks.astral.agent.exception

internal typealias ExceptionsState = MutableList<Throwable>

infix fun ExceptionsState.catch(block: () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
        this += e
    }
}
