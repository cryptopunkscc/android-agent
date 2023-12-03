package cc.cryptopunks.wrapdrive.offer

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.cryptopunks.wrapdrive.EmptyPeerOffer
import cc.cryptopunks.wrapdrive.OfferId
import cc.cryptopunks.wrapdrive.OffersRepository
import cc.cryptopunks.wrapdrive.PeerOffer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
internal class OfferViewModel(
    private val repository: OffersRepository,
    private val errors: MutableList<Exception>,
) : ViewModel() {

    private val _offerId = MutableStateFlow("")
    val offerId = _offerId.asStateFlow()

    val offer: StateFlow<PeerOffer> = offerId.transformLatest { id ->
        emit(EmptyPeerOffer)
        if (id.isNotBlank()) merge(
            repository.incoming,
            repository.outgoing,
        ).mapNotNull { list ->
            list.find { peerOffer ->
                peerOffer.offer.id == id
            }
        }.collect(this)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = EmptyPeerOffer
    )

    fun set(id: OfferId) {
        _offerId.value = id
    }

    fun download() {
        val id = offerId.value
        if (id.isBlank()) return
        viewModelScope.launch {
            try {
                repository.download(id)
            } catch (e: Exception) {
                errors += e
            }
        }
    }
}

internal fun OfferViewModel.setOfferId(intent: Intent?) {
    set(intent?.data?.lastPathSegment.orEmpty())
}
