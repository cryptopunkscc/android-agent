package cc.cryptopunks.astral.agent.node.handler.notification

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cc.cryptopunks.astral.agent.R
import cc.cryptopunks.astral.bind.android.Action
import cc.cryptopunks.astral.bind.android.NotifyServiceApi
import cc.cryptopunks.astral.bind.android.Progress
import cc.cryptopunks.astral.bind.android.Channel as NotificationChannel
import cc.cryptopunks.astral.bind.android.Intent as AstralIntent
import cc.cryptopunks.astral.bind.android.Notification as AstralNotification

class Notifier(
    private val context: Context,
) : NotifyServiceApi {

    private val manager: NotificationManagerCompat = NotificationManagerCompat.from(context)

    override fun string(): String {
        return "android/notify/jrpc"
    }

    override fun create(channel: NotificationChannel) {
        val compat = NotificationChannelCompat
            .Builder(channel.id, channel.importance.toInt())
            .setName(channel.name)
            .build()
        manager.createNotificationChannel(compat)
    }

    override fun notify(notification: AstralNotification) = try {
        context.notify(manager, notification)
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}

@SuppressLint("MissingPermission")
private fun Context.notify(manager: NotificationManagerCompat, notification: AstralNotification) {
    manager.notify(notification.id.toInt() + 100, notification.compat(this))
}

private fun AstralNotification.compat(context: Context) = NotificationCompat
    .Builder(context, channelId)
    .setContentTitle(contentTitle)
    .setContentText(contentText)
    .setContentInfo(contentInfo)
    .setTicker(ticker)
    .setSubText(subText)
    .setNumber(number.toInt())
    .setAutoCancel(autoCancel)
    .setSmallIcon(resolveIconId(smallIcon))
    .setOngoing(ongoing)
    .setOnlyAlertOnce(onlyAlertOnce)
    .setDefaults(defaults.toInt())
    .setSilent(silent)
    .setGroup(group)
    .setGroupSummary(groupSummary)
    .setPriority(priority.toInt())
    .setContentIntent(contentIntent?.android(context))
    .addAction(action?.android(context))
    .setProgress(progress)
    .build()

private fun AstralIntent.android(context: Context): PendingIntent {
    val action = action.ifEmpty { Intent.ACTION_VIEW }
    val intent = Intent(action, Uri.parse(uri)).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    return when (type) {
        "service" -> PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        else -> PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }
}

private fun Action.android(context: Context): NotificationCompat.Action {
    return NotificationCompat.Action(resolveIconId(icon), title, intent?.android(context))
}

private fun NotificationCompat.Builder.setProgress(progress: Progress?) = apply {
    progress?.run {
        setProgress(max.toInt(), current.toInt(), indeterminate)
    }
}

private fun resolveIconId(key: String?): Int = R.drawable.baseline_notification_important_black_24dp
