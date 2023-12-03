package cc.cryptopunks.wrapdrive

import cc.cryptopunks.wrapdrive.offer.OfferViewModel
import cc.cryptopunks.wrapdrive.offers.OffersViewModel
import cc.cryptopunks.wrapdrive.share.ShareViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val warpdriveModule = module {
    // view models
    viewModelOf(::OffersViewModel)
    viewModelOf(::OfferViewModel)
    viewModelOf(::ShareViewModel)

    // repository
    singleOf(::WarpdriveStatus)
    singleOf(::OffersRepository)
    singleOf(::AgentPeersRepository).bind<PeersRepository>()

    // api
    factoryOf(::JrpcWrapdriveClient).bind<WarpdriveClient>()
}
