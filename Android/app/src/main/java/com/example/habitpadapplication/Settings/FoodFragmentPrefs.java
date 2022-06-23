package com.example.habitpadapplication.Settings;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.TimePicker;

import com.example.habitpadapplication.R;

import java.util.Calendar;

@SuppressLint("ValidFragment")
public class FoodFragmentPrefs extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener{

    private FoodReminderActivity mActivity;
    private Preference foodNotiEnablePref,fromFoodTimePrf;
    private Preference foodLunchNotiEnablePref,fromFoodLunchTimePrf;
    private Preference foodDinnerNotiEnablePref,fromFoodDinnerTimePrf;
    private Preference foodSnackNotiEnablePref,fromFoodSnackTimePrf;
    private Calendar foodFromC = Calendar.getInstance();
    private Calendar foodLunchFromC = Calendar.getInstance();
    private Calendar foodDinnerFromC = Calendar.getInstance();
    private Calendar foodSnackFromC = Calendar.getInstance();

    //variables
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public FoodFragmentPrefs(Context _context){
        context = _context;
        sharedPreferences = _context.getSharedPreferences("userFoodReminderSession",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }



    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.food_fragment_prefs);


        foodNotiEnablePref= findPreference(PreferenceKey.PREF_FOOD_IS_ENABLED);
        fromFoodTimePrf =  findPreference(PreferenceKey.PREF_FOOD_START_TIME);

        foodLunchNotiEnablePref= findPreference(PreferenceKey.PREF_FOOD_LUNCH_IS_ENABLED);
        fromFoodLunchTimePrf =  findPreference(PreferenceKey.PREF_FOOD_LUNCH_START_TIME);

        foodDinnerNotiEnablePref= findPreference(PreferenceKey.PREF_FOOD_DINNER_IS_ENABLED);
        fromFoodDinnerTimePrf =  findPreference(PreferenceKey.PREF_FOOD_DINNER_START_TIME);

        foodSnackNotiEnablePref= findPreference(PreferenceKey.PREF_FOOD_SNACK_IS_ENABLED);
        fromFoodSnackTimePrf =  findPreference(PreferenceKey.PREF_FOOD_SNACK_START_TIME);



        foodNotiEnablePref.setOnPreferenceChangeListener(this);
        foodLunchNotiEnablePref.setOnPreferenceChangeListener(this);
        foodDinnerNotiEnablePref.setOnPreferenceChangeListener(this);
        foodSnackNotiEnablePref.setOnPreferenceChangeListener(this);

        initSummaries();



        fromFoodTimePrf.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {
                int hour = foodFromC.get(Calendar.HOUR_OF_DAY);
                int minutes = foodFromC.get(Calendar.MINUTE);
                TimePickerDialog datePickerDialog =
                        new TimePickerDialog(getActivity(),foodTimeFrom ,hour,minutes, true);
                datePickerDialog.show();
                return false;
            }
        });

        fromFoodLunchTimePrf.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {
                int hour = foodLunchFromC.get(Calendar.HOUR_OF_DAY);
                int minutes = foodLunchFromC.get(Calendar.MINUTE);
                TimePickerDialog datePickerDialog =
                        new TimePickerDialog(getActivity(),foodLunchTimeFrom ,hour,minutes, true);
                datePickerDialog.show();
                return false;
            }
        });

        fromFoodDinnerTimePrf.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {
                int hour = foodDinnerFromC.get(Calendar.HOUR_OF_DAY);
                int minutes = foodDinnerFromC.get(Calendar.MINUTE);
                TimePickerDialog datePickerDialog =
                        new TimePickerDialog(getActivity(),foodDinnerTimeFrom ,hour,minutes, true);
                datePickerDialog.show();
                return false;
            }
        });

        fromFoodSnackTimePrf.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {
                int hour = foodSnackFromC.get(Calendar.HOUR_OF_DAY);
                int minutes = foodSnackFromC.get(Calendar.MINUTE);
                TimePickerDialog datePickerDialog =
                        new TimePickerDialog(getActivity(),foodSnackTimeFrom ,hour,minutes, true);
                datePickerDialog.show();
                return false;
            }
        });


    }
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity!=null) {
            mActivity=(FoodReminderActivity) activity;
        }

    }

    private void initSummaries() {
        boolean isNotifEnabled = getPreferenceManager().getSharedPreferences().getBoolean(PreferenceKey.PREF_FOOD_IS_ENABLED, false);
        boolean isLunchNotifEnabled = getPreferenceManager().getSharedPreferences().getBoolean(PreferenceKey.PREF_FOOD_LUNCH_IS_ENABLED, false);
        boolean isDinnerNotifEnabled = getPreferenceManager().getSharedPreferences().getBoolean(PreferenceKey.PREF_FOOD_DINNER_IS_ENABLED, false);
        boolean isSnackNotifEnabled = getPreferenceManager().getSharedPreferences().getBoolean(PreferenceKey.PREF_FOOD_SNACK_IS_ENABLED, false);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String foodFrom = sharedPreferences.getString(PreferenceKey.FROM_FOOD_KEY,"8:0");

        SharedPreferences sharedPreferencesLunch = PreferenceManager.getDefaultSharedPreferences(context);
        String foodLunchFrom = sharedPreferencesLunch.getString(PreferenceKey.FROM_FOOD_LUNCH_KEY,"8:0");

        SharedPreferences sharedPreferencesDinner = PreferenceManager.getDefaultSharedPreferences(context);
        String foodDinnerFrom = sharedPreferencesDinner.getString(PreferenceKey.FROM_FOOD_DINNER_KEY,"8:0");

        SharedPreferences sharedPreferencesSnack = PreferenceManager.getDefaultSharedPreferences(context);
        String foodSnackFrom = sharedPreferencesSnack.getString(PreferenceKey.FROM_FOOD_SNACK_KEY,"8:0");


        fromFoodTimePrf.setSummary(foodFrom);
        foodNotiEnablePref.setSummary(getString(isNotifEnabled));
        enableNotificationsPrefs(isNotifEnabled);

        fromFoodLunchTimePrf.setSummary(foodLunchFrom);
        foodLunchNotiEnablePref.setSummary(getString(isLunchNotifEnabled));
        enableLunchNotificationsPrefs(isLunchNotifEnabled);

        fromFoodDinnerTimePrf.setSummary(foodDinnerFrom);
        foodDinnerNotiEnablePref.setSummary(getString(isDinnerNotifEnabled));
        enableDinnerNotificationsPrefs(isDinnerNotifEnabled);

        fromFoodSnackTimePrf.setSummary(foodSnackFrom);
        foodSnackNotiEnablePref.setSummary(getString(isSnackNotifEnabled));
        enableSnackNotificationsPrefs(isSnackNotifEnabled);

    }

    TimePickerDialog.OnTimeSetListener foodTimeFrom = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            foodFromC.set(Calendar.HOUR_OF_DAY,hourOfDay);
            foodFromC.set(Calendar.MINUTE,minute);
            fromFoodTimePrf.setSummary(String.valueOf(hourOfDay)+":"+String.valueOf(minute));
            setTimePref(PreferenceKey.FROM_FOOD_KEY,String.valueOf(hourOfDay)+":"+String.valueOf(minute));
        }
    };

    TimePickerDialog.OnTimeSetListener foodLunchTimeFrom = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            foodLunchFromC.set(Calendar.HOUR_OF_DAY,hourOfDay);
            foodLunchFromC.set(Calendar.MINUTE,minute);
            fromFoodLunchTimePrf.setSummary(String.valueOf(hourOfDay)+":"+String.valueOf(minute));
            setTimePref(PreferenceKey.FROM_FOOD_LUNCH_KEY,String.valueOf(hourOfDay)+":"+String.valueOf(minute));
        }
    };

    TimePickerDialog.OnTimeSetListener foodDinnerTimeFrom = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            foodDinnerFromC.set(Calendar.HOUR_OF_DAY,hourOfDay);
            foodDinnerFromC.set(Calendar.MINUTE,minute);
            fromFoodDinnerTimePrf.setSummary(String.valueOf(hourOfDay)+":"+String.valueOf(minute));
            setTimePref(PreferenceKey.FROM_FOOD_DINNER_KEY,String.valueOf(hourOfDay)+":"+String.valueOf(minute));
        }
    };

    TimePickerDialog.OnTimeSetListener foodSnackTimeFrom = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            foodSnackFromC.set(Calendar.HOUR_OF_DAY,hourOfDay);
            foodSnackFromC.set(Calendar.MINUTE,minute);
            fromFoodSnackTimePrf.setSummary(String.valueOf(hourOfDay)+":"+String.valueOf(minute));
            setTimePref(PreferenceKey.FROM_FOOD_SNACK_KEY,String.valueOf(hourOfDay)+":"+String.valueOf(minute));
        }
    };


    private  String getString(boolean b){
        if (b)
            return "ON";
        return "OFF";

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()) {

            case PreferenceKey.PREF_FOOD_IS_ENABLED:
                enableNotificationsPrefs(newValue);
                setSwitchPrefSummaries(newValue.toString(), preference);
                return true;

            case PreferenceKey.PREF_FOOD_LUNCH_IS_ENABLED:
                enableLunchNotificationsPrefs(newValue);
                setSwitchPrefSummaries(newValue.toString(), preference);
                return true;

            case PreferenceKey.PREF_FOOD_DINNER_IS_ENABLED:
                enableDinnerNotificationsPrefs(newValue);
                setSwitchPrefSummaries(newValue.toString(), preference);
                return true;

            case PreferenceKey.PREF_FOOD_SNACK_IS_ENABLED:
                enableSnackNotificationsPrefs(newValue);
                setSwitchPrefSummaries(newValue.toString(), preference);
                return true;

        }

        return true;
    }

    private void enableNotificationsPrefs(Object newValue) {

        if(newValue.equals(true))
        {
            fromFoodTimePrf.setEnabled(true);
        }
        else{
            fromFoodTimePrf.setEnabled(false);
        }
    }

    private void enableLunchNotificationsPrefs(Object newValue) {

        if(newValue.equals(true))
        {
            fromFoodLunchTimePrf.setEnabled(true);
        }
        else{
            fromFoodLunchTimePrf.setEnabled(false);
        }
    }

    private void enableDinnerNotificationsPrefs(Object newValue) {

        if(newValue.equals(true))
        {
            fromFoodDinnerTimePrf.setEnabled(true);
        }
        else{
            fromFoodDinnerTimePrf.setEnabled(false);
        }
    }

    private void enableSnackNotificationsPrefs(Object newValue) {

        if(newValue.equals(true))
        {
            fromFoodSnackTimePrf.setEnabled(true);
        }
        else{
            fromFoodSnackTimePrf.setEnabled(false);
        }
    }



    private void setSwitchPrefSummaries(String newValue, Preference preference) {
        if(newValue.equals("true"))
        {  preference.setSummary("ON ");
        }
        else
            preference.setSummary("OFF ");
    }



    private void setTimePref(String command, String str){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(command, str);
        editor.commit();


    }

    public void logoutUserFromFoodReminderSession(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().clear().commit();
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.clear();
//        editor.commit();

    }

}