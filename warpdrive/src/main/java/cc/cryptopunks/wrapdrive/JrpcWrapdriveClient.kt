package cc.cryptopunks.wrapdrive

import cc.cryptopunks.astral.apphost.GsonAppHostClient
import cc.cryptopunks.astral.apphost.decodeList
import cc.cryptopunks.astral.apphost.decodeMessage
import cc.cryptopunks.astral.apphost.encodeLine
import cc.cryptopunks.astral.apphost.gsonSerializer
import cc.cryptopunks.astral.apphost.use
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.isActive

class JrpcWrapdriveClient(
    private val client: GsonAppHostClient,
) : WarpdriveClient {

    private fun conn() = client.query("warpdrive").gsonSerializer()

    override suspend fun createOffer(peerId: PeerId, filePath: String): Offer = conn().use {
        encodeLine(listOf("createOffer", peerId, filePath))
        decodeMessage()
    }

    override suspend fun acceptOffer(id: OfferId): Unit = conn().use {
        encodeLine(listOf("acceptOffer", id))
        decodeMessage()
    }

    override suspend fun listOffers(filter: Filter): List<Offer> = conn().use {
        encodeLine(listOf("listOffers", filter))
        decodeList()
    }

    override fun listenOffers(filter: Filter): Flow<Offer> =
        listen("listenOffers", filter)

    override fun listenStatus(filter: Filter): Flow<Status> =
        listen("listenStatus", filter)

    private inline fun <reified T> listen(
        method: String,
        filter: Filter,
    ): Flow<T> = channelFlow {
        conn().use {
            encodeLine(listOf(method, filter))
            while (isActive) {
                val offer = decodeMessage<T>()
                send(offer)
            }
        }
    }

    override suspend fun listPeers(): List<Peer> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePeer(peer: PeerId, attr: String, value: String) {
        TODO("Not yet implemented")
    }
}
