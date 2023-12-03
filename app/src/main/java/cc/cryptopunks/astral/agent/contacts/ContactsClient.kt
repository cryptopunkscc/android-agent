package cc.cryptopunks.astral.agent.contacts

import cc.cryptopunks.astral.agent.api.Contact
import cc.cryptopunks.astral.apphost.GsonAppHostClient
import cc.cryptopunks.astral.apphost.decodeList
import cc.cryptopunks.astral.apphost.decodeMessage
import cc.cryptopunks.astral.apphost.encodeLine
import cc.cryptopunks.astral.apphost.use
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.isActive

class ContactsClient(
    private val client: GsonAppHostClient,
) {
    fun flow() = channelFlow {
        client.query("contacts").use {
            encodeLine(listOf("contacts"))
            while (isActive) {
                val contact = decodeList<Contact>()
                send(contact)
            }
        }
    }

    suspend fun presenceList() = client.query("contacts").use {
        encodeLine(listOf("listPresence"))
        decodeList<Contact>()
    }

    fun presenceFlow() = channelFlow {
        client.query("contacts").use {
            encodeLine(listOf("presence"))
            while (isActive) {
                val contact = decodeMessage<Contact>()
                send(contact)
            }
        }
    }
}
