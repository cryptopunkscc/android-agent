package cc.cryptopunks.astral.agent.contacts

import cc.cryptopunks.astral.apphost.AppHostClient
import cc.cryptopunks.astral.apphost.Serializer
import cc.cryptopunks.astral.apphost.decodeList
import cc.cryptopunks.astral.apphost.serializer
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive

interface LinksRepository {
    val contacts: Flow<List<Link>>
}

internal class AstralLinksRepository(
    private val client: AppHostClient,
    private val serializer: Serializer,
) : LinksRepository {

    override val contacts = MutableStateFlow<List<Link>>(emptyList())

    suspend fun observe() = coroutineScope {

        val conn = client.query("contacts").serializer(serializer)

        conn.runCatching {
            while (isActive) {
                val list = decodeList<Link>()
                contacts.emit(list)
            }
        }.onFailure {
            conn.close()
            it.printStackTrace()
        }
    }
}
