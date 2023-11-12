package cc.cryptopunks.astral.agent.node

import astral.Astral
import cc.cryptopunks.astral.apphost.AppHostClient
import cc.cryptopunks.astral.apphost.ApphostListener
import cc.cryptopunks.astral.apphost.Conn as AstralConn
import cc.cryptopunks.astral.apphost.QueryData

internal fun AppHostClient(): AppHostClient = AppHostClientAdapter()

internal class Conn(conn: astral.Conn) : astral.Conn by conn, AstralConn

private class AppHostClientAdapter : AppHostClient {
    private val client: astral.AppHostClient = Astral.newApphostClient()
    override fun query(nodeId: String, query: String) = Conn(client.query(nodeId, query))
    override fun register(name: String) = AppHostListenerAdapter(client.register(name))
    override fun resolve(name: String): String = client.resolve(name)
}

private class AppHostListenerAdapter(
    private val listener: astral.ApphostListener,
) : ApphostListener {
    override fun next() = QueryDataAdapter(listener.next())
}

private class QueryDataAdapter(
    private val data: astral.QueryData,
) : QueryData {
    override fun accept() = Conn(data.accept())
    override fun caller(): String = data.caller()
    override fun query(): String = data.query()
    override fun reject() = data.reject()
}

