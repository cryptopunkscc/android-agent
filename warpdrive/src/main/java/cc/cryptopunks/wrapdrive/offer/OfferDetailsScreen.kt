package cc.cryptopunks.wrapdrive.offer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cc.cryptopunks.wrapdrive.Info
import cc.cryptopunks.wrapdrive.Offer
import cc.cryptopunks.wrapdrive.Peer
import cc.cryptopunks.wrapdrive.PeerOffer
import cc.cryptopunks.wrapdrive.StatusFailed
import cc.cryptopunks.wrapdrive.compose.PreviewBox
import cc.cryptopunks.wrapdrive.compose.PreviewModel
import cc.cryptopunks.wrapdrive.formattedName
import cc.cryptopunks.wrapdrive.formattedStatus
import cc.cryptopunks.wrapdrive.requireWritePermissions
import cc.cryptopunks.wrapdrive.shortOfferId
import java.text.DateFormat
import java.text.SimpleDateFormat
import kotlin.random.Random

@Composable
internal fun OfferDetailsScreen(
    model: OfferViewModel = viewModel(),
) {
    val data by model.offer.collectAsStateWithLifecycle()
    val context = LocalContext.current
    OfferDetailsScreen(data = data) {
        context.requireWritePermissions {
            model.download()
        }
    }
}

@Preview
@Composable
internal fun OfferDetailsPreview() = PreviewBox {
    OfferDetailsScreen(
        PeerOffer(
            peer = Peer(),
            offer = Offer(
                id = "OID-7fc4-44f4-62d3",
                create = System.currentTimeMillis() - 100000,
                update = System.currentTimeMillis(),
                peer = PreviewModel.peer.id,
                files = (0..10).map {
                    Info(
                        uri = "/asdasd" + if (it == 0) "" else "/$it",
                        size = if (it == 0) 0 else Random.nextLong(Int.MAX_VALUE.toLong()),
                        isDir = it == 0
                    )
                },
                status = StatusFailed
            ),
        )
    ) {

    }
}

@Composable
private fun OfferDetailsScreen(
    data: PeerOffer,
    onDownloadClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize(),
    ) {
        OfferHeaderView(data)
        FileItemsView(
            offer = data.offer,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
        if (data.offer.isIncoming)
            DownloadButton(onDownloadClick)
    }
}

@Composable
private fun OfferHeaderView(
    data: PeerOffer,
) {
    val (peer, offer) = data
    OfferHeaderView(
        offerId = shortOfferId(offer.id),
        contactName = PeerOffer(peer, offer).formattedName,
        status = offer.formattedStatus,
        createdAt = offer.create,
        updateAt = offer.update,
    )
}

@Composable
private fun OfferHeaderView(
    offerId: String,
    contactName: String,
    status: String,
    createdAt: Long,
    updateAt: Long,
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = offerId,
                style = MaterialTheme.typography.h6
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = contactName,
                style = MaterialTheme.typography.subtitle1
            )
        }
        Spacer(Modifier.height(16.dp))
        val dateOffset = 120.dp
        Box {
            Text(
                text = "Created at",
                style = MaterialTheme.typography.subtitle2,
            )
            Spacer(Modifier.width(32.dp))
            Text(
                text = dateTime.format(createdAt),
                modifier = Modifier.offset(x = dateOffset),
            )
        }
        Box {
            Text(
                text = status,
                style = MaterialTheme.typography.subtitle2,
            )
            Text(
                text = when (createdAt) {
                    updateAt -> ""
                    else -> dateTime.format(updateAt)
                },
                modifier = Modifier.offset(x = dateOffset),
            )
        }
    }
}

@Composable
private fun DownloadButton(
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier.padding(8.dp),
        onClick = onClick,
    ) {
        Text(
            text = "Download".uppercase(),
            style = MaterialTheme.typography.button
        )
    }
}

private val dateTime: DateFormat = SimpleDateFormat.getDateTimeInstance()
