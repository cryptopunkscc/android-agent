package cc.cryptopunks.astral.agent.node.handler

import cc.cryptopunks.astral.agent.node.Conn
import cc.cryptopunks.astral.apphost.ConnSerializer
import cc.cryptopunks.astral.apphost.Serializer
import cc.cryptopunks.astral.apphost.gsonSerializer
import cc.cryptopunks.astral.apphost.serializer
import cc.cryptopunks.astral.bind.astral.Handlers
import cc.cryptopunks.astral.bind.astral.Conn as AstralConn
import cc.cryptopunks.astral.bind.astral.Handler as AstralHandler

typealias Method = ConnSerializer.() -> Unit

typealias Methods = Map<String, Method>

class Handlers(vararg methods: Methods) : Handlers {

    private val iterator = methods
        .reduce(Methods::plus)
        .map { (name, serve) -> Handler(name, gsonSerializer, serve) }
        .iterator()

    override fun next(): AstralHandler? =
        iterator.run { if (hasNext()) next() else null }
}

private class Handler(
    private val name: String,
    private val enc: Serializer,
    private val method: Method,
) : AstralHandler {

    override fun string(): String = name

    override fun serve(conn: AstralConn) {
        try {
            Conn(conn).serializer(enc).method()
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            conn.close()
        }
    }
}
