package cc.cryptopunks.astral.agent.api

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.startAgentActivity() = startActivity(
    Intent(Intent.ACTION_VIEW, Uri.parse("astral://main"))
)

fun Context.startAgentService() = startService(
    Intent("cc.cryptopunks.astral.agent.main.AstraldService").apply {
        `package` = "cc.cryptopunks.astral.agent"
    }
)
