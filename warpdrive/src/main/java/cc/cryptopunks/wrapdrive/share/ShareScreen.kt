package cc.cryptopunks.wrapdrive.share

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cc.cryptopunks.wrapdrive.Offer
import cc.cryptopunks.wrapdrive.R
import cc.cryptopunks.wrapdrive.StatusAccepted
import cc.cryptopunks.wrapdrive.compose.PreviewBox
import cc.cryptopunks.wrapdrive.compose.WarpdriveConnectionView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

@Composable
fun ShareScreen(
    shareModel: ShareViewModel = viewModel(),
    contacts: @Composable ((String) -> Unit) -> Unit = {},
    selectUri: () -> Unit = {},
) {
    val isSharing by shareModel.isSharing.collectAsStateWithLifecycle()
    val uriTime by shareModel.uri.collectAsStateWithLifecycle()
    val uri = uriTime.first

    ShareScreen(
        isSharing = isSharing,
        uri = uri,
        results = shareModel.results,
        contacts = contacts,
        share = shareModel::share,
        selectUri = selectUri,
    )
}

@Preview
@Composable
fun ShareScreenPreview() = PreviewBox {
    ShareScreen(
        isSharing = false,
        uri = Uri.parse("warpdrive://share/preview"),
        results = flowOf(Result.success(Offer())),
        contacts = {},
        share = {}
    )
}

@Composable
fun ShareScreen(
    isSharing: Boolean,
    uri: Uri,
    results: Flow<Result<Offer>> = emptyFlow(),
    contacts: @Composable ((String) -> Unit) -> Unit = {},
    share: (String) -> Unit = {},
    selectUri: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Warp Share") },
                actions = {
                    if (isSharing) {
                        CircularProgressIndicator(
                            color = LocalContentColor.current.copy(LocalContentAlpha.current),
                            modifier = Modifier
                                .size(24.dp),
                            strokeWidth = 3.dp
                        )
                        Spacer(Modifier.width(14.dp))
                    }
                    ShareButton(selectUri)
                }
            )
        },
    ) { paddingValues ->
        Box(
            contentAlignment = Alignment.BottomStart,
            modifier = Modifier.padding(paddingValues)
        ) {
            WarpdriveConnectionView {
                contacts(share)
            }
            SnackbarHost(snackbarHostState)
        }
    }

    val context = LocalContext.current
    val astralPackage = stringResource(id = R.string.astral_package)

    LaunchedEffect(uri) {
        when (uri) {
            Uri.EMPTY -> snackbarHostState.showSnackbar(
                message = "Cannot obtain file uri, please select file once again",
                duration = SnackbarDuration.Indefinite,
            )

            else -> {
                snackbarHostState.currentSnackbarData?.dismiss()
                context.grantUriPermission(
                    astralPackage, uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        }
    }
    LaunchedEffect(results) {
        results.collect { result ->
            var message = ""
            result.onSuccess { offer ->
                message = when (offer.status) {
                    StatusAccepted -> "Share accepted, the files are sending in background"
                    else -> "Share delivered and waiting for approval"
                }
            }
            result.onFailure {
                message = "Cannot share files: " + it.localizedMessage
            }
            snackbarHostState.showSnackbar(message)
        }
    }
}
