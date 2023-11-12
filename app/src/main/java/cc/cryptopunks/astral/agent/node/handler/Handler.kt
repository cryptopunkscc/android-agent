package cc.cryptopunks.astral.agent.node.handler

import cc.cryptopunks.astral.agent.node.Conn
import cc.cryptopunks.astral.apphost.ConnSerializer
import cc.cryptopunks.astral.apphost.Serializer
import cc.cryptopunks.astral.apphost.gsonSerializer
import cc.cryptopunks.astral.apphost.serializer

typealias Method = ConnSerializer.() -> Unit

typealias Methods = Map<String, Method>

class Handlers(vararg methods: Methods) : astral.Handlers {

    private val iterator = methods
        .reduce(Methods::plus)
        .map { (name, serve) -> Handler(name, gsonSerializer, serve) }
        .iterator()

    override fun next(): astral.Handler? =
        iterator.run { if (hasNext()) next() else null }
}

private class Handler(
    private val name: String,
    private val enc: Serializer,
    private val method: Method,
) : astral.Handler {

    override fun string(): String = name

    override fun serve(conn: astral.Conn) {
        try {
            Conn(conn).serializer(enc).method()
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            conn.close()
        }
    }
}
