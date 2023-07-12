package cc.cryptopunks.astral.agent

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService

internal fun Service.startForegroundNotification() {
    val channelId = createNotificationChannel(
        id = "astral",
        channelName = "Astral Service",
        importance = NotificationManagerCompat.IMPORTANCE_LOW,
        color = Color.BLUE,
        visibility = Notification.VISIBILITY_PRIVATE,
    )

    val pendingIntent: PendingIntent = PendingIntent
        .getActivity(this, 0, astralActivityIntent, PendingIntent.FLAG_IMMUTABLE)

    val builder = NotificationCompat
        .Builder(this, channelId)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentIntent(pendingIntent)
        .setContentTitle("Astral")

    startForeground(1, builder.build())
}

//internal fun Context.showConfigureAstralNotification() {
//    val channelId = createNotificationChannel(
//        id = "info",
//        channelName = "info",
//        importance = NotificationManagerCompat.IMPORTANCE_MAX,
//        color = Color.BLUE,
//        visibility = Notification.VISIBILITY_PRIVATE,
//    )
//
//    val pendingIntent: PendingIntent = PendingIntent
//        .getActivity(this, 0, astralActivityIntent, PendingIntent.FLAG_IMMUTABLE)
//
//    val builder = NotificationCompat
//        .Builder(this, channelId)
//        .setSmallIcon(R.mipmap.ic_launcher)
//        .setContentIntent(pendingIntent)
//        .setContentTitle("Astral")
//        .setContentText("Configuration required")
//        .setAutoCancel(true)
//        .setPriority(NotificationCompat.PRIORITY_MAX)
//
//    NotificationManagerCompat.from(this).notify(2, builder.build())
//}

val astralActivityIntent = Intent(Intent.ACTION_VIEW, Uri.parse("astral://main"))

private fun Context.createNotificationChannel(
    id: String,
    channelName: String,
    importance: Int,
    color: Int,
    visibility: Int,
): String {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        -> NotificationChannel(id, channelName, importance).apply {
            lightColor = color
            lockscreenVisibility = visibility
        }.also { channel ->
            getSystemService<NotificationManager>()
                ?.createNotificationChannel(channel)
                ?: throw Exception("Cannot obtain NotificationManager")
        }.id
        else
        -> NotificationChannelCompat.Builder(id, importance).apply {
            setLightColor(color)
        }.build().also { channel ->
            getSystemService<NotificationManagerCompat>()
                ?.createNotificationChannel(channel)
                ?: throw Exception("Cannot obtain NotificationManagerCompat")
        }.id
    }
}