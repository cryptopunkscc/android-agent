package cc.cryptopunks.wrapdrive.offers

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cc.cryptopunks.astral.agent.api.ext
import cc.cryptopunks.wrapdrive.offer.OfferViewModel
import cc.cryptopunks.wrapdrive.offer.setOfferId
import org.koin.androidx.viewmodel.ext.android.viewModel

class OffersActivity : ComponentActivity() {

    private val offerModel: OfferViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        offerModel.setOfferId(intent)
        setContent {
            ext.Theme {
                OffersScreen()

                val id by offerModel.offerId.collectAsState()
                if (id.isNotBlank()) BackHandler {
                    intent = null
                    offerModel.set("")
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        this.intent = intent
        offerModel.setOfferId(intent)
    }
}
