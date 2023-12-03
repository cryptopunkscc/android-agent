package cc.cryptopunks.astral.agent.compose

import cc.cryptopunks.astral.agent.api.ComposeApi
import cc.cryptopunks.astral.agent.contacts.ContactsScreen
import cc.cryptopunks.astral.agent.exception.ErrorsScreen
import org.koin.dsl.module

val composeModule = module {
    single {
        ComposeApi(
            Theme = { AstralTheme(it) },
            Errors = { ErrorsScreen() },
            Contacts = { ContactsScreen(select = it) }
        )
    }
}
