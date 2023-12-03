package cc.cryptopunks.astral.agent.node.handler.notification

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import cc.cryptopunks.astral.agent.node.handler.Methods
import cc.cryptopunks.astral.apphost.byte
import cc.cryptopunks.astral.apphost.decodeList
import cc.cryptopunks.astral.apphost.decodeMessage
import cc.cryptopunks.astral.apphost.gsonSerializer

fun Context.notificationManagerMethods(): Methods {
    val manager: NotificationManagerCompat = NotificationManagerCompat.from(this)
    return mapOf(
        Notification.channel to {
            val channel = gsonSerializer().decodeMessage<Notification.Channel>()
            manager.create(channel)
            byte = 0
        },
        Notification.notify to {
            while (true) {
                val notifications = gsonSerializer().decodeList<Notification>()
                notify(manager, notifications)
                byte = 0
            }
        }
    )
}
