package com.example.mysportfriends_school_project;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationBroadCast extends BroadcastReceiver {

    public static final String MY_CHANNEL_ID = "MY_CHANNEL_ID7";

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        SportingActivityClass sportingActivity = (SportingActivityClass) intent.getSerializableExtra("sportingActivity");

        if (sportingActivity == null) {
        } else {
            String title = sportingActivity.getTitle();
            String time = sportingActivity.getTime();
            createAndShowNotification(context, "כותרת הפעולה: " + title + " :בזמן:   " + time, "");
        }
    }

    @SuppressLint("MissingPermission")
    private void createAndShowNotification(Context context, String title, String text) {
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "התראות";
            String description = "התראות פרויקט";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(MY_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context, MY_CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        Intent activityIntent = new Intent(context, WelcomeUserActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent,PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        builder
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }
}