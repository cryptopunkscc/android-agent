package cc.cryptopunks.wrapdrive.offers

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cc.cryptopunks.wrapdrive.FilterIn
import cc.cryptopunks.wrapdrive.FilterOut
import cc.cryptopunks.wrapdrive.PeerOffer
import cc.cryptopunks.wrapdrive.compose.PreviewBox
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Preview
@Composable
private fun DashboardPreview() = PreviewBox {
//    DashboardView(PreviewModel.instance) // TODO
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun OffersDashboardContent(
    offers: List<Flow<List<PeerOffer>>>,
    onItemClick: (PeerOffer) -> Unit,
) {
    Column {
        val pagerState = rememberPagerState()
        val currentPage = pagerState.currentPage
        val scope = rememberCoroutineScope()
        LaunchedEffect(currentPage) { pagerState.animateScrollToPage(currentPage) }

        TabRow(
            selectedTabIndex = currentPage,
            backgroundColor = MaterialTheme.colors.background,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            }
        ) {
            pages.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title.uppercase()) },
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                )
            }
        }

        HorizontalPager(
            count = filters.size,
            state = pagerState
        ) { page ->
            val items by offers[page].collectAsState(emptyList())
            OfferItems(
                offers = items,
                filter = filters[page],
                onItemClick = onItemClick,
            )
        }
    }
}

private val pages = listOf("received", "send")
private val filters = listOf(FilterIn, FilterOut)
