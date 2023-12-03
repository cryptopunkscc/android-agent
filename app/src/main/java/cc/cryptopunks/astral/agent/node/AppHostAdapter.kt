package cc.cryptopunks.astral.agent.node

import cc.cryptopunks.astral.apphost.AppHostClient
import cc.cryptopunks.astral.apphost.ApphostListener
import cc.cryptopunks.astral.apphost.QueryData
import cc.cryptopunks.astral.bind.astral.Astral
import cc.cryptopunks.astral.apphost.Conn as AstralConn
import cc.cryptopunks.astral.bind.astral.AppHostClient as AstralAppHostClient
import cc.cryptopunks.astral.bind.astral.ApphostListener as AstralApphostListener
import cc.cryptopunks.astral.bind.astral.QueryData as AstralQueryData

internal fun AppHostClient(): AppHostClient = AppHostClientAdapter()

internal class Conn(private val conn: cc.cryptopunks.astral.bind.astral.Conn?) : AstralConn {
    override fun close() = conn!!.close()
    override fun read(buff: ByteArray): Int = conn!!.read(buff).toInt()
    override fun readN(n: Int): ByteArray = conn!!.readN(n.toLong())
    override fun write(buff: ByteArray): Int = conn!!.write(buff).toInt()
}

private class AppHostClientAdapter : AppHostClient {
    private val client: AstralAppHostClient = Astral.newApphostClient()
    override fun query(query: String, nodeId: String) =
        Conn(client.query(nodeId.takeIf(String::isNotBlank) ?: client.resolve("localnode"), query))

    override fun register(name: String) = AppHostListenerAdapter(client.register(name))
    override fun resolve(name: String): String = client.resolve(name)
}

private class AppHostListenerAdapter(
    private val listener: AstralApphostListener,
) : ApphostListener {
    override fun next() = QueryDataAdapter(listener.next())
}

private class QueryDataAdapter(
    private val data: AstralQueryData,
) : QueryData {
    override fun accept() = Conn(data.accept())
    override fun caller(): String = data.caller()
    override fun query(): String = data.query()
    override fun reject() = data.reject()
}

