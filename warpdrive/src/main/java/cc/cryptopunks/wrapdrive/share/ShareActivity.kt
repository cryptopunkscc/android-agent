package cc.cryptopunks.wrapdrive.share

import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import cc.cryptopunks.astral.agent.api.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShareActivity : ComponentActivity() {

    private val model: ShareViewModel by viewModel()

    private val selectUri = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri == null) finish()
        else model.setUri(uri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUri(intent)
        setContent {
            inject.Theme {
                ShareScreen(
                    shareModel = model,
                    contacts = inject.Contacts,
                    selectUri = { selectUri.startFileChooser() }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        this.intent = intent
        setUri(intent)
    }

    private fun setUri(intent: Intent) {
        val uri = intent.clipData?.items()?.firstOrNull()?.uri
        if (uri != null) {
            model.setUri(uri)
        } else {
            selectUri.startFileChooser()
        }
    }
}

private fun ActivityResultLauncher<String>.startFileChooser() = launch("*/*")

private fun ClipData.items(): List<ClipData.Item> = List(itemCount, this::getItemAt)
