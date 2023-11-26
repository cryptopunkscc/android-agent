package cc.cryptopunks.astral.agent.node.handler.notification

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cc.cryptopunks.astral.agent.R

class Notifier(
    private val context: Context,
) : astral.Notifier {

    private val manager: NotificationManagerCompat = NotificationManagerCompat.from(context)

    override fun string(): String {
        return "android/notify/jrpc"
    }

    override fun create(channel: astral.NotificationChannel) {
        val compat = NotificationChannelCompat
            .Builder(channel.id, channel.importance.toInt())
            .setName(channel.name)
            .build()
        manager.createNotificationChannel(compat)

    }

    override fun notify(notification: astral.Notification) {
        context.notify(manager, notification)
    }
}

@SuppressLint("MissingPermission")
private fun Context.notify(manager: NotificationManagerCompat, notification: astral.Notification) {
    manager.notify(notification.id.toInt() + 100, notification.compat(this))
}

private fun astral.Notification.compat(context: Context) = NotificationCompat
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

private fun astral.AndroidIntent.android(context: Context): PendingIntent {
    val action = action.ifEmpty { Intent.ACTION_VIEW }
    val intent = Intent(action, Uri.parse(uri)).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    return when (type) {
        "service" -> PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        else -> PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}

private fun astral.NotificationAction.android(context: Context): NotificationCompat.Action {
    return NotificationCompat.Action(resolveIconId(icon), title, contentIntent?.android(context))
}

private fun NotificationCompat.Builder.setProgress(progress: astral.NotificationProgress?) = apply {
    progress?.run {
        setProgress(max.toInt(), current.toInt(), indeterminate)
    }
}

private fun resolveIconId(key: String?): Int = R.drawable.baseline_notification_important_black_24dp
