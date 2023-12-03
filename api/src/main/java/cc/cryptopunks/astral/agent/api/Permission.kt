package cc.cryptopunks.astral.agent.api

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import cc.cryptopunks.astral.agent.api.Permissions.Key.Message
import cc.cryptopunks.astral.agent.api.Permissions.Key.Rejected
import cc.cryptopunks.astral.agent.api.Permissions.Key.Required

object Permissions {

    object Key {
        const val Message = "message"
        const val Required = "request"
        const val Rejected = "rejected"
    }

    fun request(
        message: String,
        vararg required: String,
    ) = Intent(ACTION_VIEW, Uri.parse("astral://permissions")).apply {
        putExtra(Message, message)
        putExtra(Required, required)
    }

    fun result(
        rejected: Array<String>,
    ) = Intent().apply {
        putExtra(Rejected, rejected)
    }

    fun getMessage(intent: Intent): String =
        intent.getStringExtra(Message) ?: ""

    fun getRequired(intent: Intent): Array<String> =
        intent.getStringArrayExtra(Required) ?: emptyArray()

    fun getRejected(intent: Intent): Array<String> =
        intent.getStringArrayExtra(Rejected) ?: emptyArray()
}
