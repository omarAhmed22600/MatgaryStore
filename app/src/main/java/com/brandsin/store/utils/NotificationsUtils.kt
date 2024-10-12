package com.brandsin.store.utils

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import timber.log.Timber
import android.graphics.Color
import com.brandsin.store.R
import com.brandsin.store.ui.activity.auth.AuthActivity

object NotificationsUtils {

    private const val NOTIFICATIONS_CHANNEL_ID = "NOTIFICATIONS_CHANNEL_ID"
    private const val NOTIFICATIONS_NOTIFICATION_ID = 48
    private const val SALARY_NOTIFICATIONS_CHANNEL_ID = "SALARY_NOTIFICATIONS_CHANNEL_ID"
    private const val SALARY_NOTIFICATIONS_NOTIFICATION_ID = 49
    private const val REPORTS_NOTIFICATIONS_CHANNEL_ID = "REPORTS_NOTIFICATIONS_CHANNEL_ID"
    private const val REPORTS_NOTIFICATIONS_NOTIFICATION_ID = 50
    private const val VACATIONS_NOTIFICATIONS_CHANNEL_ID = "VACATIONS_NOTIFICATIONS_CHANNEL_ID"
    private const val VACATIONS_NOTIFICATIONS_NOTIFICATION_ID = 51
    private const val UPDATE_PROFILE_NOTIFICATIONS_CHANNEL_ID =
        "UPDATE_PROFILE_NOTIFICATIONS_CHANNEL_ID"
    private const val UPDATE_PROFILE_NOTIFICATIONS_NOTIFICATION_ID = 52
    private const val TEAM_OBSERVATION_NOTIFICATIONS_CHANNEL_ID =
        "TEAM_OBSERVATION_NOTIFICATIONS_CHANNEL_ID"
    private const val TEAM_OBSERVATION_NOTIFICATIONS_NOTIFICATION_ID = 53

    fun getMainActivityPendingIntent(applicationContext: Context): PendingIntent {
        var mainActivityClazz: Class<*> = AuthActivity::class.java
        val intent = Intent(applicationContext, mainActivityClazz)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        return PendingIntent.getActivity(
            applicationContext,
            0 /* Request code */,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun showNotification(
        pendingIntent: PendingIntent,
        appContext: Context,
        title: String,
        body: String,
        type: String,
        sound: String?,
    ) {
        val uri = if (sound.isNullOrEmpty() || sound == "default") null else {
            val indexOfDot = sound.indexOf(".")

            val resSound: Int = appContext.resources.getIdentifier(
                if (indexOfDot == -1) sound else sound.substring(0, indexOfDot).apply {
                    Timber.e("NotificationsService -> string $this")
                },
                "raw",
                appContext.packageName
            )

            Timber.e("NotificationsService -> resSound $resSound")

            if (resSound == 0) null else Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(appContext.packageName)
                .appendPath(resSound.toString())
                .build()
        }

        val (channelId, channelName, notificationId) =
            when (type) {
                "" -> Triple(
                    NOTIFICATIONS_CHANNEL_ID,
                    appContext.getString(R.string.notifications),
                    NOTIFICATIONS_NOTIFICATION_ID
                )

                "chat" -> Triple(
                    SALARY_NOTIFICATIONS_CHANNEL_ID,
                    appContext.getString(R.string.chat),
                    SALARY_NOTIFICATIONS_NOTIFICATION_ID
                )

                "order" -> Triple(
                    REPORTS_NOTIFICATIONS_CHANNEL_ID,
                    appContext.getString(R.string.my_orders),
                    REPORTS_NOTIFICATIONS_NOTIFICATION_ID
                )

                "refund" -> Triple(
                    VACATIONS_NOTIFICATIONS_CHANNEL_ID,
                    appContext.getString(R.string.refundable_reason),
                    VACATIONS_NOTIFICATIONS_NOTIFICATION_ID
                )


                else -> Triple(
                    REPORTS_NOTIFICATIONS_CHANNEL_ID,
                    appContext.getString(R.string.my_orders),
                    REPORTS_NOTIFICATIONS_NOTIFICATION_ID
                )


            }
        showNotificationToLaunchPendingIntent(
            pendingIntent,
            appContext,
            title,
            body,
            channelId,
            channelName,
            notificationId,
            uri
        )
    }

    @SuppressLint("NewApi")
    private fun showNotificationToLaunchPendingIntent(
        pendingIntent: PendingIntent,
        applicationContext: Context,
        title: String,
        body: String,
        channelId: String,
        channelName: String,
        notificationId: Int,
        uri: Uri?,
    ) {
        Timber.e("NotificationsService -> uri $uri")
        val icon = BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.ic_launcher)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)
            .setContentInfo(title)
            .setLargeIcon(icon)
            .setColor(applicationContext.resources.getColor(R.color.color_primary))
            .setLights(Color.RED, 1000, 300)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setSmallIcon(R.mipmap.ic_launcher);
        /* val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
             .setSmallIcon(R.drawable.ic_launcher)
             .setContentTitle("My Notification")
             .setContentText("This is a notification.")
             .setPriority(NotificationCompat.PRIORITY_MAX)
             .setDefaults(NotificationCompat.DEFAULT_SOUND)*/

        val notificationManager =
            applicationContext.getSystemService<NotificationManager>() ?: return

        // Since android Oreo notification channel is needed.
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        val audioAttrs = AudioAttributes.Builder()
            /*
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
             */
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_INSTANT)
            .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
            .build()
        channel.setSound(uri ?: defaultSoundUri, audioAttrs)
        notificationBuilder.setChannelId(channelId)
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

}
