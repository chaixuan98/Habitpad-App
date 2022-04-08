package com.example.habitpadapplication;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.habitpadapplication.BroadcastReceivers.StopNotificationBroadcastReceiver;
import com.example.habitpadapplication.Notifications.NotificationReciever;
import com.example.habitpadapplication.Settings.PreferenceKey;

import java.util.Calendar;



public class AlarmHelper {


    public static void setNotificationsAlarm(Context mContext) {


        boolean isAlarmUp = (PendingIntent.getBroadcast(mContext, 101, new Intent(mContext, NotificationReciever.class)
                , PendingIntent.FLAG_UPDATE_CURRENT)!= null);
              if(isAlarmUp) {
                 stopNotificationsAlarm(mContext);
                }
                  SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                  String from = prefs.getString(PreferenceKey.FROM_KEY, "8:0");
                  int interval = prefs.getInt(PreferenceKey.PREF_INTERVAL, 2);
                  String[] values = from.split(":");
                  String hr = values[0];
                  String mt = values[1];

                  Calendar calendar = Calendar.getInstance();
                  Calendar now = Calendar.getInstance();

                  calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hr));
                  calendar.set(Calendar.MINUTE, Integer.valueOf(mt));
                  calendar.set(Calendar.SECOND, 0);
                  Intent nIntent = new Intent(mContext, NotificationReciever.class);
                  nIntent.putExtra("title", "Water Reminder");
                  nIntent.putExtra("text", "It is time to track your water intake");
                  PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 101, nIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                  AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);

                  if (now.after(calendar)) {
                      alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, now.getTimeInMillis()+600000, 1000 * 60 * 60 * interval, pendingIntent);
                  } else {
                      alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * interval, pendingIntent);
                  }

              }


    public static void stopNotificationsAlarm(Context mContext){
        AlarmManager alarmManager = (AlarmManager)mContext.getSystemService(ALARM_SERVICE);
        Intent mIntent = new Intent(mContext,NotificationReciever.class);
        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(mContext,105,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        if(alarmManager!=null)
        {  alarmManager.cancel(pendingIntent);}

    }

    public static void setCancelNotificationAlarm(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String to = prefs.getString(PreferenceKey.TO_KEY, "20:0");
        String[] values = to.split(":");
        String hr = values[0];
        String mt = values[1];
        Calendar cal= Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hr));
        cal.set(Calendar.MINUTE, Integer.valueOf(mt));
        cal.set(Calendar.SECOND, 0);
        Intent nIntent = new Intent(context, StopNotificationBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 104, nIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),pendingIntent);


    }


    public static void setFoodNotificationsAlarm(Context mContext) {

        boolean isAlarmUp = (PendingIntent.getBroadcast(mContext, 102, new Intent(mContext, NotificationReciever.class)
                , PendingIntent.FLAG_UPDATE_CURRENT)!= null);
        if(isAlarmUp) {
            stopNotificationsAlarm(mContext);
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String from = prefs.getString(PreferenceKey.FROM_FOOD_KEY, "8:0");
        String[] values = from.split(":");
        String hr = values[0];
        String mt = values[1];

        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hr));
        calendar.set(Calendar.MINUTE, Integer.valueOf(mt));
        calendar.set(Calendar.SECOND, 0);
        Intent nIntent = new Intent(mContext, NotificationReciever.class);
        nIntent.putExtra("title", "Food Reminder");
        nIntent.putExtra("text", "It is time to track your breakfast");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 102, nIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);

        if (now.after(calendar)) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, now.getTimeInMillis()+600000 , pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , pendingIntent);
        }

    }

    public static void setFoodLunchNotificationsAlarm(Context mContext) {

        boolean isAlarmUp = (PendingIntent.getBroadcast(mContext, 106, new Intent(mContext, NotificationReciever.class)
                , PendingIntent.FLAG_UPDATE_CURRENT)!= null);
        if(isAlarmUp) {
            stopNotificationsAlarm(mContext);
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String from = prefs.getString(PreferenceKey.FROM_FOOD_LUNCH_KEY, "8:0");
        String[] values = from.split(":");
        String hr = values[0];
        String mt = values[1];

        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hr));
        calendar.set(Calendar.MINUTE, Integer.valueOf(mt));
        calendar.set(Calendar.SECOND, 0);
        Intent nIntent = new Intent(mContext, NotificationReciever.class);
        nIntent.putExtra("title", "Food Reminder");
        nIntent.putExtra("text", "It is time to track your lunch");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 106, nIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);

        if (now.after(calendar)) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, now.getTimeInMillis()+600000 , pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , pendingIntent);
        }
    }

    public static void setFoodDinnerNotificationsAlarm(Context mContext) {

        boolean isAlarmUp = (PendingIntent.getBroadcast(mContext, 107, new Intent(mContext, NotificationReciever.class)
                , PendingIntent.FLAG_UPDATE_CURRENT)!= null);
        if(isAlarmUp) {
            stopNotificationsAlarm(mContext);
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String from = prefs.getString(PreferenceKey.FROM_FOOD_DINNER_KEY, "8:0");
        String[] values = from.split(":");
        String hr = values[0];
        String mt = values[1];

        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hr));
        calendar.set(Calendar.MINUTE, Integer.valueOf(mt));
        calendar.set(Calendar.SECOND, 0);
        Intent nIntent = new Intent(mContext, NotificationReciever.class);
        nIntent.putExtra("title", "Food Reminder");
        nIntent.putExtra("text", "It is time to track your dinner");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 107, nIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);

        if (now.after(calendar)) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, now.getTimeInMillis()+600000 , pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , pendingIntent);
        }
    }

    public static void setFoodSnackNotificationsAlarm(Context mContext) {

        boolean isAlarmUp = (PendingIntent.getBroadcast(mContext, 108, new Intent(mContext, NotificationReciever.class)
                , PendingIntent.FLAG_UPDATE_CURRENT)!= null);
        if(isAlarmUp) {
            stopNotificationsAlarm(mContext);
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String from = prefs.getString(PreferenceKey.FROM_FOOD_SNACK_KEY, "8:0");
        String[] values = from.split(":");
        String hr = values[0];
        String mt = values[1];

        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hr));
        calendar.set(Calendar.MINUTE, Integer.valueOf(mt));
        calendar.set(Calendar.SECOND, 0);
        Intent nIntent = new Intent(mContext, NotificationReciever.class);
        nIntent.putExtra("title", "Food Reminder");
        nIntent.putExtra("text", "It is time to track your snack");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 108, nIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);

        if (now.after(calendar)) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, now.getTimeInMillis()+600000 , pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , pendingIntent);
        }
    }


    public static void setWorkoutNotificationsAlarm(Context mContext) {

        boolean isAlarmUp = (PendingIntent.getBroadcast(mContext, 103, new Intent(mContext, NotificationReciever.class)
                , PendingIntent.FLAG_UPDATE_CURRENT)!= null);
        if(isAlarmUp) {
            stopNotificationsAlarm(mContext);
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String from = prefs.getString(PreferenceKey.FROM_WORKOUT_KEY, "8:0");
        String[] values = from.split(":");
        String hr = values[0];
        String mt = values[1];

        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hr));
        calendar.set(Calendar.MINUTE, Integer.valueOf(mt));
        calendar.set(Calendar.SECOND, 0);
        Intent nIntent = new Intent(mContext, NotificationReciever.class);
        nIntent.putExtra("title", "Workout Reminder");
        nIntent.putExtra("text", "It is time to track your workout");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 103, nIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);

        //alarmManager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()  ,pendingIntent);

        if (now.after(calendar)) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, now.getTimeInMillis()+600000 , pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , pendingIntent);
        }

    }

}

