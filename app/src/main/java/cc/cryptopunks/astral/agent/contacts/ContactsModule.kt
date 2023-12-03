package cc.cryptopunks.astral.agent.contacts

import cc.cryptopunks.astral.agent.api.Contact
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val contactsModule = module {
    singleOf(::ContactsRepository).bind<Contact.Repository>()
    factoryOf(::ContactsClient)
}
