package cc.cryptopunks.wrapdrive

import cc.cryptopunks.astral.agent.api.Contact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.transformLatest

@OptIn(
    ExperimentalCoroutinesApi::class,
    FlowPreview::class,
)
internal class OffersRepository(
    private val client: WarpdriveClient,
    private val peers: PeersRepository,
    private val scope: CoroutineScope,
    private val warpdriveStatus: WarpdriveStatus,
) {

    val incoming = flow(FilterIn)
    val outgoing = flow(FilterOut)

        private fun flow(filter: Filter) = warpdriveStatus.transformLatest { connected ->
        if (connected) {
            client.listOffers(filter).forEach { emit(it) }
            merge(
                client.listenStatus(filter),
                client.listenOffers(filter),
            ).collect(this)
        }
    }.retry {
        delay(3000)
        true
    }.map { next ->
        if (next is Offer) PeerOffer(
            offer = next,
            peer = peers.get(next.peer)
                ?: Peer(id = next.peer)
        ) else next
    }.scan(mapOf<OfferId, PeerOffer>()) { acc, value ->
        when (value) {
            is PeerOffer -> acc + (value.offer.id to value)
            is Status -> acc[value.id]?.run {
                val update = value.id to copy(
                    offer = offer.copy(
                        progress = value.progress,
                        update = value.update,
                        status = value.status,
                        index = value.index,
                    )
                )
                acc + update
            } ?: acc

            else -> acc
        }
    }.map { map ->
        map.values.sortedByDescending { peerOffer ->
            peerOffer.offer.create
        }
    }.debounce(150).shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(3000),
        replay = 1,
    )

    suspend fun download(offerId: OfferId) {
        client.acceptOffer(offerId)
    }
}

interface PeersRepository {
    suspend fun get(peerId: PeerId): Peer?
}

internal class AgentPeersRepository(
    private val linksRepository: Contact.Repository,
) : PeersRepository {

    override suspend fun get(peerId: PeerId): Peer =
        linksRepository.flow.flatMapLatest { links ->
            links.asFlow()
        }.first { link ->
            link.id == peerId
        }.run {
            Peer(id = id, alias = alias)
        }
}
