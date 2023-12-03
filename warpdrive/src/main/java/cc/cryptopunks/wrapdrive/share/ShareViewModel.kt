package cc.cryptopunks.wrapdrive.share

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.cryptopunks.wrapdrive.Offer
import cc.cryptopunks.wrapdrive.WarpdriveClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class ShareViewModel(
    private val client: WarpdriveClient,
) : ViewModel() {

    val uri = MutableStateFlow(Uri.EMPTY to 0L)
    val isSharing = MutableStateFlow(false)
    val results = MutableSharedFlow<Result<Offer>>(extraBufferCapacity = 1)


    fun setUri(uri: Uri) {
        this.uri.value = uri to System.currentTimeMillis()
    }

    fun share(peerId: String) {
        val uri = uri.value.first
        if (uri != Uri.EMPTY) viewModelScope.launch {
            try {
                isSharing.value = true
                val result = withTimeout(10000) {
                    client.createOffer(peerId, uri.toString())
                }
                results.emit(Result.success(result))
            } catch (e: Throwable) {
                results.emit(Result.failure(e))
            } finally {
                isSharing.value = false
            }
        }
    }
}
