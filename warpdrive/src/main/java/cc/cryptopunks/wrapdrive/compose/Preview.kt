package cc.cryptopunks.wrapdrive.compose

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import cc.cryptopunks.wrapdrive.Info
import cc.cryptopunks.wrapdrive.Offer
import cc.cryptopunks.wrapdrive.Peer
import cc.cryptopunks.wrapdrive.StatusFailed
import kotlin.random.Random

object PreviewModel {
    val peer = Peer(
        id = "1kj2h312kj3h",
        alias = "Peer name",
    )
    val offer = Offer(
        id = "OID-7fc4-44f4-62d3",
        create = System.currentTimeMillis() - 100000,
        update = System.currentTimeMillis(),
        peer = peer.id,
        files = (0..10).map {
            Info(
                uri = "/asdasd" + if (it == 0) "" else "/$it",
                size = if (it == 0) 0 else Random.nextLong(Int.MAX_VALUE.toLong()),
                isDir = it == 0
            )
        },
        status = StatusFailed
    )
//    val update = OffersViewModel.Update(
//        offers = (0..10).map { offer },
//        peers = mapOf(peer.id to peer)
//    )

//    val instance: OffersModel
//        get() {
//            return OffersModel().apply {
//                current.value = PeerOffer(peer, offer)
//                updates[FilterIn]?.value = update
//                updates[FilterOut]?.value = update
//            }
//        }
}

@Composable
fun PreviewBox(content: @Composable () -> Unit) = Surface {
    content()
}
