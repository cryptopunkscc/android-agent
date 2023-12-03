package cc.cryptopunks.wrapdrive.offer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cc.cryptopunks.wrapdrive.Info
import cc.cryptopunks.wrapdrive.Offer
import cc.cryptopunks.wrapdrive.compose.HorizontalDivider
import cc.cryptopunks.wrapdrive.compose.PreviewModel
import cc.cryptopunks.wrapdrive.formatSize

@Preview
@Composable
fun FileItemsPreview() {
    FileItemsView(
        offer = PreviewModel.offer,
        Modifier.fillMaxSize()
    )
}

@Composable
fun FileItemsView(
    offer: Offer,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
    ) {
        item { HorizontalDivider() }
        itemsIndexed(offer.files) { index, item: Info ->
            HorizontalDivider {
                FileItem(
                    name = item.name,
                    size = item.size,
                    progress = when {
                        index > offer.index -> 0
                        index < offer.index -> item.size
                        else -> offer.progress
                    },
                )
            }
        }
    }
}

@Composable
private fun FileItem(
    name: String,
    progress: Long,
    size: Long,
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.subtitle2
        )
        Text(
            text = "${progress.formatSize()}/${size.formatSize()}",
            style = MaterialTheme.typography.caption
        )
    }
}
