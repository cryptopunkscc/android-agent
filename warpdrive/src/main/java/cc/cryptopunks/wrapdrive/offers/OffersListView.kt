package cc.cryptopunks.wrapdrive.offers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cc.cryptopunks.wrapdrive.Filter
import cc.cryptopunks.wrapdrive.FilterIn
import cc.cryptopunks.wrapdrive.PeerOffer
import cc.cryptopunks.wrapdrive.compose.HorizontalDivider
import cc.cryptopunks.wrapdrive.compose.PreviewBox
import cc.cryptopunks.wrapdrive.compose.PreviewModel
import cc.cryptopunks.wrapdrive.formattedAmount
import cc.cryptopunks.wrapdrive.formattedDateTime
import cc.cryptopunks.wrapdrive.formattedInfo
import cc.cryptopunks.wrapdrive.formattedName
import cc.cryptopunks.wrapdrive.formattedSize
import cc.cryptopunks.wrapdrive.shortOfferId

@Preview
@Composable
private fun OfferItemsPreview() = PreviewBox {
    OfferItems(
        offers = listOf(
            PeerOffer(
                peer = PreviewModel.peer,
                offer = PreviewModel.offer,
            )
        ),
        filter = FilterIn,
    )
}

@Composable
fun OfferItems(
    offers: List<PeerOffer>,
    filter: Filter,
    onItemClick: (PeerOffer) -> Unit = {},
) {
    if (offers.isEmpty()) NoOffersView(filter)
    else LazyColumn(Modifier.fillMaxSize()) {
        items(
            items = offers,
            key = { it.offer.id },
            contentType = { Unit }
        ) { item ->
            HorizontalDivider {
                OfferItem(item) {
                    onItemClick(item)
                }
            }
        }
    }
}

@Composable
private fun OfferItem(
    item: PeerOffer,
    onClick: () -> Unit,
) {
    Column(
        Modifier
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row {
            Text(
                text = shortOfferId(item.offer.id),
                style = MaterialTheme.typography.subtitle2
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = item.formattedName,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        Text(
            text = item.offer.formattedInfo,
            style = MaterialTheme.typography.subtitle1,
        )
        Spacer(Modifier.height(6.dp))
        Row {
            Text(
                text = item.offer.formattedSize,
                style = MaterialTheme.typography.body2,
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = item.offer.formattedAmount,
                style = MaterialTheme.typography.body2,
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = item.offer.formattedDateTime,
                style = MaterialTheme.typography.caption,
            )
        }
    }
}
