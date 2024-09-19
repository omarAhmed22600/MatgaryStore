package com.brandsin.store.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.brandsin.store.R;
import com.brandsin.store.ui.activity.auth.AuthActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class NotificationFirebaseMessagingService extends FirebaseMessagingService {

    boolean showNotification;
    Intent intent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        showNotification = PrefMethods.INSTANCE.getIsNotificationsEnabled(this);

        // Check if Notification are Enabled?
        if (showNotification) {
            //Show Notification
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            Map<String, String> data = remoteMessage.getData();

            sendNotification(notification, data);
        }
    }

    private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {

        // Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo);

        if (intent == null) {
            intent = new Intent();
        }

        if (data != null) {
            if (data.get("order_id") != null) {
                intent = new Intent(this, AuthActivity.class);
                intent.putExtra("order_id", data.get("order_id"));
            } else {
                intent = new Intent(this, AuthActivity.class);
            }
        } else {
            intent = new Intent(this, AuthActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        if (notification != null) {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
                    .setContentTitle(notification.getTitle())
                    .setContentText(notification.getBody())
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent)
                    .setContentInfo(notification.getTitle())
                    .setLargeIcon(icon)
                    .setSmallIcon(R.drawable.app_logo) // .setSmallIcon(R.mipmap.ic_launcher)
                    // .setColor(getResources().getColor(R.color.color_primary))
                    .setLights(Color.RED, 1000, 300)
                    .setDefaults(Notification.DEFAULT_VIBRATE);

            try {
                String picture_url = data.get("picture_url");
                if (picture_url != null && !"".equals(picture_url)) {
                    URL url = new URL(picture_url);
                    Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    notificationBuilder.setStyle(
                            new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(notification.getBody())
                    );
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Notification Channel is required for Android O and above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        "channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT
                );
                channel.setDescription("channel description");
                channel.setShowBadge(true);
                channel.canShowBadge();
                channel.enableLights(true);
                channel.setLightColor(Color.RED);
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0, notificationBuilder.build());
        }
    }
}