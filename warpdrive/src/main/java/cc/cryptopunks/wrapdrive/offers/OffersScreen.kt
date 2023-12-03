package cc.cryptopunks.wrapdrive.offers

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cc.cryptopunks.astral.agent.api.ext
import cc.cryptopunks.wrapdrive.EmptyPeerOffer
import cc.cryptopunks.wrapdrive.compose.WarpdriveConnectionView
import cc.cryptopunks.wrapdrive.offer.OfferDetailsScreen
import cc.cryptopunks.wrapdrive.offer.OfferViewModel
import cc.cryptopunks.wrapdrive.share.ShareButton
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
internal fun OffersScreen(
    modifier: Modifier = Modifier,
    offersModel: OffersViewModel = koinViewModel(),
    offerModel: OfferViewModel = koinViewModel(),
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Warp Drive")
                },
                actions = {
                    ShareButton()
                }
            )
        },
    ) { paddingValues ->
        WarpdriveConnectionView(
            modifier = modifier.padding(paddingValues),
        ) {
            OffersDashboardContent(offersModel.offers) { peerOffer ->
                offerModel.set(peerOffer.offer.id)
            }
            val offer by offerModel.offer.collectAsState()
            if (offer != EmptyPeerOffer) OfferDetailsScreen(offerModel)
        }
    }
    ext.Errors(koinInject())
}
