package cc.cryptopunks.wrapdrive.download

import android.app.Activity
import android.os.Bundle
import cc.cryptopunks.wrapdrive.WarpdriveClient
import cc.cryptopunks.wrapdrive.requireWritePermissions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class DownloadActivity : Activity() {

    private val client: WarpdriveClient by inject()
    private val scope: CoroutineScope by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireWritePermissions {
            try {
                val offerId = intent.data!!.lastPathSegment!!
                scope.launch { client.acceptOffer(offerId) }
            } catch (e: Throwable) {
                println("Cannot download files")
                e.printStackTrace()
            }
        }
        finishAndRemoveTask()
    }
}
