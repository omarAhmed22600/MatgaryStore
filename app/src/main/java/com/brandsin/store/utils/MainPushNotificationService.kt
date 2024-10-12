package com.brandsin.store.utils

import android.app.PendingIntent
import android.content.Intent
import com.brandsin.store.ui.activity.auth.AuthActivity
import timber.log.Timber


class MainPushNotificationService : BasePushNotificationService() {
    override fun onMessageReceivedGetNotificationPendingIntent(
        title: String,
        body: String,
        type: String,
        targetId: Int?,
    ): PendingIntent {
        Timber.e("passed type is $type\nand passed id is $targetId")

        return when (type) {
           "chat" -> {
               Timber.e("chat navigation selected")
               val intent = Intent(this, AuthActivity::class.java)
               intent.putExtra("chat_id", targetId.toString())
//               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
               PendingIntent.getActivity(
                   this, 0 /* Request code */, intent,
                   PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
               )
           }
            "order" -> {
                val intent = Intent(this, AuthActivity::class.java)
                intent.putExtra("order_id", targetId.toString())
                PendingIntent.getActivity(
                    this, 0, intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            }

            "refund" -> {
                val intent = Intent(this, AuthActivity::class.java)
                intent.putExtra("refundable_id", targetId.toString())
                PendingIntent.getActivity(
                    this, 0, intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
            else -> {val intent = Intent(this, AuthActivity::class.java)
                PendingIntent.getActivity(
                    this, 0, intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )}
        }
    }
}