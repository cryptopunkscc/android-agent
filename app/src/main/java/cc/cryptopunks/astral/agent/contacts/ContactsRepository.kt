package cc.cryptopunks.astral.agent.contacts

import cc.cryptopunks.astral.agent.api.Contact
import cc.cryptopunks.astral.agent.node.AstralStatus
import cc.cryptopunks.astral.agent.node.astralStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest

@OptIn(ExperimentalCoroutinesApi::class)
class ContactsRepository(
    private val client: ContactsClient,
    scope: CoroutineScope,
) : Contact.Repository {

    override val flow: StateFlow<List<Contact>> = astralStatus.map { status ->
        status == AstralStatus.Started
    }.transformLatest { connected ->
        if (connected) client.flow().map {
            client.presenceList().toSet().plus(it).toList()
        }.collect(this)
    }.scan(emptyList<Contact>()) { accumulator, value ->
        accumulator.toSet().plus(value).toList()
    }.retry {
        delay(3000)
        true
    }.stateIn(scope, SharingStarted.WhileSubscribed(3000), emptyList())
}
