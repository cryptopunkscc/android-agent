package cc.cryptopunks.astral.agent.log

import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import kotlinx.coroutines.launch
import org.koin.androidx.compose.navigation.koinNavViewModel
import org.koin.compose.koinInject

@Composable
fun LogScreen(
    model: LogViewModel = koinNavViewModel(),
    preferences: LogPreferences = koinInject(),
) {
    val text by model.logText.collectAsState()
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val softWrap by preferences.softWrap
    if (!scrollState.canScrollForward) LaunchedEffect(text.length) {
        scope.launch {
            scrollState.scrollBy(scrollState.scrollBy(Float.MAX_VALUE))
        }
    }
    LaunchedEffect(Unit) {
        scrollState.scrollBy(scrollState.scrollBy(Float.MAX_VALUE))
    }
    Box(
        modifier = if (softWrap) Modifier else Modifier
            .fillMaxSize()
            .horizontalScroll(rememberScrollState()),
    ) {
        SelectionContainer {
            Text(
                text = text,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                softWrap = softWrap,
                fontFamily = FontFamily.Monospace,
            )
        }
    }
}
