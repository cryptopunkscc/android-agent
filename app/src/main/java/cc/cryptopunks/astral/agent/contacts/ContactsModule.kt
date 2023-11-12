package cc.cryptopunks.astral.agent.contacts

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val contactsModule = module {
    viewModelOf(::ContactsViewModel)
    singleOf(::AstralLinksRepository).bind<LinksRepository>()
}
