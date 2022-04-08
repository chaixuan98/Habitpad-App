package com.example.habitpadapplication.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefsHelper {


    public static boolean getNotificationsPrefs(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return  prefs.getBoolean(PreferenceKey.PREF_IS_ENABLED,true);

    }

    public static boolean getSoundsPrefs(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(PreferenceKey.PREF_SOUND,false);
    }

    public static boolean getFoodNotificationsPrefs(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return  prefs.getBoolean(PreferenceKey.PREF_FOOD_IS_ENABLED,true);
    }

    public static boolean getFoodLunchNotificationsPrefs(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return  prefs.getBoolean(PreferenceKey.PREF_FOOD_LUNCH_IS_ENABLED,true);
    }

    public static boolean getFoodDinnerNotificationsPrefs(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return  prefs.getBoolean(PreferenceKey.PREF_FOOD_DINNER_IS_ENABLED,true);
    }
    public static boolean getFoodSnackNotificationsPrefs(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return  prefs.getBoolean(PreferenceKey.PREF_FOOD_SNACK_IS_ENABLED,true);
    }

    public static boolean getWorkoutNotificationsPrefs(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return  prefs.getBoolean(PreferenceKey.PREF_WORKOUT_IS_ENABLED,true);

    }

    public static String getGlassSizePrefs(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(PreferenceKey.PREF_GLASS_SIZE,"50");

    }
    public static String getCoffeeSizePrefs(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(PreferenceKey.PREF_COFFEE_SIZE,"100");

    }
    public static String getTeaSizePrefs(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(PreferenceKey.PREF_TEA_SIZE,"150");

    }
    public static String getColaSizePrefs(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(PreferenceKey.PREF_COLA_SIZE,"200");

    }
    public static String getJuiceSizePrefs(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        return  prefs.getString(PreferenceKey.PREF_JUICE_SIZE, "250");
    }

//    public static boolean getFirstTimeRunPrefs(Context context){
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
//        return pref.getBoolean("first_time_run", true) ;
//    }
//
//    public static void setFirstTimeRunPrefs(Context context, boolean b) {
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putBoolean("first_time_run", b);
//        editor.commit();
//    }

}
