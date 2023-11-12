package cc.cryptopunks.astral.agent.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ContactsViewModel(
    linksRepository: LinksRepository,
) : ViewModel() {

    val state: StateFlow<List<Contact>> = linksRepository.contacts
        .map { links -> links.map(Link::toContact) }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}

private fun Link.toContact() = Contact(
    id = remoteId,
    alias = remote,
)
