package com.brandsin.store.utils

import android.app.PendingIntent
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

abstract class BasePushNotificationService : FirebaseMessagingService() {

    /**
     * @return `null` to do nothing as a result, otherwise launch the notification with that
     * [PendingIntent].
     */
    abstract fun onMessageReceivedGetNotificationPendingIntent(
        title: String, body: String, type: String, targetId: Int?
    ): PendingIntent?

    final override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.e("NotificationsService  -> " +
                "Got a remote message -> ${remoteMessage.data}\n${
                    remoteMessage.data.toList().joinToString(".\n.") {
                        "${it.first} : ${it.second}"
                    }
                }"
        )
        val model = getModelFromMap(remoteMessage.data)

        Timber.e("investigate1 -> model -> $model")

        Timber.e("NotificationsService -> $model")

        val title = remoteMessage.notification?.title.orEmpty()
        val body = remoteMessage.notification?.body.orEmpty()
/*
        Timber.e("type ${model!!.type}\n${model.targetId}")
*/

        if (model?.type == "chat") {
            Timber.e("investigate1 -> on receive chat sending broadcast")
            Intent().also { intent ->
                intent.action = REFRESH_CHAT
                intent.putExtra(CHAT, model.targetId)
                sendBroadcast(intent)
            }
            Timber.e("investigate1 -> sent broadcast -> ${model.targetId}")
        }
        Timber.e("investigate1 -> data -> ${remoteMessage.data}")

        val pendingIntent = onMessageReceivedGetNotificationPendingIntent(
            title, body, model?.type.orEmpty(), model?.targetId,
        )

        NotificationsUtils.showNotification(
            pendingIntent ?: return,
            applicationContext,
            title,
            body,
            model?.type.orEmpty(),
            model?.sound.orEmpty()
        )
    }

    /**
     * example -> target_id=63, body= تم إضافة خدمات إضافية للطلب, type=order, sound=default, title= خدمات إضافية}
     */
    private fun getModelFromMap(map: Map<String, String>): NotificationModel? {
        Timber.e("it is ${map["wallet_id"]}")
        val title = map["title"].orEmpty()
        val body = map["body"].orEmpty()
        val type = if (map["chat_id"] != null) {
            "chat"
        } else if (map["order_id"] != null) {
            "order"
        }else if (map["refundable_id"] != null) {
            "refund"
        }else ""
        Timber.e("type is $type")
        //val sound = map.getValue("sound")
        val targetId =
            if (map["chat_id"] != null)
                map["chat_id"]!!.toInt()
            else if (map["order_id"] != null)
                map["order_id"]!!.toInt()
            else if (map["refundable_id"] != null)
                map["refundable_id"]!!.toInt()
            else -1
        return NotificationModel(title, body, type, targetId, map["sound"])
    }

    data class NotificationModel(
        var title: String,
        var body: String,
        var type: String,
        var targetId: Int,
        var sound: String?,
    )

    companion object {
        const val REFRESH_CHAT = "REFRESH_CHAT"
        const val CHAT = "CHAT"
    }


}
