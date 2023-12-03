package cc.cryptopunks.wrapdrive

import android.net.Uri
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import kotlin.math.log10
import kotlin.math.pow

internal val Offer.formattedDateTime: String
    get() = SimpleDateFormat.getDateTimeInstance().format(update)

internal val Offer.formattedSize: String
    get() = "${summaryProgress.formatSize()} / ${summarySize.formatSize()}"

internal fun Long.formatSize(): String =
    if (this <= 0) "0" + units[0]
    else (log10(toDouble()) / log10(unit)).toInt().let { digitGroup ->
        DecimalFormat("#,##0.#")
            .format(this / unit.pow(digitGroup.toDouble()))
            .toString() + units[digitGroup]
    }

private val units = arrayOf("B", "KB", "MB", "GB", "TB")
private const val unit = 1000.0


internal val Offer.formattedInfo: String
    get() = when (files.size) {
        0 -> ""
        1 -> files.first().name
        else -> when {
            !files.first().isDir -> ""
            else -> files.first().name
        }
    }.formattedUriFileName ?: ""

private val String.formattedUriFileName: String?
    get() = Uri.parse(this).run { lastPathSegment ?: host }

internal val Offer.formattedAmount: String
    get() = if (files.size > 1) "(${files.size})" else ""

internal val PeerOffer.formattedName: String
    get() = peer.takeIf { peer != EmptyPeer }
        ?.run { alias.takeIf(String::isNotEmpty) }
        ?: if (offer.peer.length == 8) shortPeerId(offer.peer)
        else offer.peer

internal val Offer.formattedStatus: String
    get() = when (status) {
        "" -> ""
        StatusAwaiting -> status.replaceFirstChar(Char::uppercase)
        else -> status.replaceFirstChar(Char::uppercase) + " at"
    }

internal val Offer.summaryProgress: Long
    get() = when (index) {
        -1 -> 0
        files.size -> files.sumOf(Info::size)
        else -> files.subList(0, index).sumOf(Info::size) + progress
    }

internal val Offer.summarySize: Long
    get() = files.sumOf(Info::size)


internal fun shortPeerId(id: PeerId): String = if (id.length < 8) ""
    else "UID-" + id.substring(0, 8).chunked(4).joinToString("-")

internal fun shortOfferId(id: OfferId): String = if (id.length < 12) ""
    else "OID-" + id.replace("-", "").substring(0, 12).chunked(4).joinToString("-")
