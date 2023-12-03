package cc.cryptopunks.wrapdrive.offers

import androidx.lifecycle.ViewModel
import cc.cryptopunks.wrapdrive.OffersRepository
import cc.cryptopunks.wrapdrive.PeerOffer
import kotlinx.coroutines.flow.Flow

internal class OffersViewModel(
    offersRepository: OffersRepository,
) : ViewModel() {

    val offers: List<Flow<List<PeerOffer>>> = listOf(
        offersRepository.incoming,
        offersRepository.outgoing,
    )
}
