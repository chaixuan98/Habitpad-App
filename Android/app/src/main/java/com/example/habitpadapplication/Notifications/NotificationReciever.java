package com.example.habitpadapplication.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.example.habitpadapplication.HomeActivity;
import com.example.habitpadapplication.R;
import com.example.habitpadapplication.Settings.PreferenceKey;


public class NotificationReciever extends BroadcastReceiver {

    private String title, message;

    @Override
    public void onReceive(Context context, Intent intent) {

        //Bundle bundle = intent.getExtras();

        title = intent.getExtras().getString("title");
        message = intent.getExtras().getString("text");

        Intent repeatingIntent = new Intent(context, HomeActivity.class);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder =setupNotification(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_id";
            NotificationChannel channel = new NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        builder.setContentIntent(pendingIntent);
        notificationManager.notify(100, builder.build());
    }

    private  Notification.Builder setupNotification(Context context) {


        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon( R.drawable.habitpadlogo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean soundEnable= prefs.getBoolean(PreferenceKey.PREF_SOUND,false);


        if(soundEnable)
        {   builder.setDefaults(Notification.DEFAULT_SOUND);}




     return builder;

    }

}
