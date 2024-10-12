package com.brandsin.store.utils

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import timber.log.Timber

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Timber.e("onNewToken - $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Timber.e("onMessageReceived message key/value pairs -> ${remoteMessage.data}")

        val (title, body) = remoteMessage.data.getValue("message").orEmpty()
            .getNotificationModel()
    }

    private data class NotificationModel(
        val title: String,
        val body: String,
    )

    private fun String.getNotificationModel(): NotificationModel {
        val jsonObject = JSONObject(this)

        val title = jsonObject.getString("title")
        val body = jsonObject.getString("body")

        return NotificationModel(title, body)
    }


}
