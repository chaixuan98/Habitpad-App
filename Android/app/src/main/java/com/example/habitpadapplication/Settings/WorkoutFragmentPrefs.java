package com.example.habitpadapplication.Settings;

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

public class WorkoutFragmentPrefs extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener{

    private WorkoutReminderActivity mActivity;
    private Context context;
    private Preference workoutNotiEnablePref,fromWorkoutTimePrf;
    private Calendar workoutFromC = Calendar.getInstance();



    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context =mActivity.getApplicationContext();
        addPreferencesFromResource(R.xml.workout_fragment_prefs);


        workoutNotiEnablePref= findPreference(PreferenceKey.PREF_WORKOUT_IS_ENABLED);
        fromWorkoutTimePrf =  findPreference(PreferenceKey.PREF_WORKOUT_START_TIME);


        workoutNotiEnablePref.setOnPreferenceChangeListener(this);

        initSummaries();



        fromWorkoutTimePrf.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {
                int hour = workoutFromC.get(Calendar.HOUR_OF_DAY);
                int minutes = workoutFromC.get(Calendar.MINUTE);
                TimePickerDialog datePickerDialog =
                        new TimePickerDialog(getActivity(),workoutTimeFrom ,hour,minutes, true);
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
            mActivity=(WorkoutReminderActivity) activity;
        }

    }

    private void initSummaries() {
        boolean isNotifEnabled = getPreferenceManager().getSharedPreferences().getBoolean(PreferenceKey.PREF_WORKOUT_IS_ENABLED, false);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String workoutFrom = sharedPreferences.getString(PreferenceKey.FROM_WORKOUT_KEY,"8:0");


        fromWorkoutTimePrf.setSummary(workoutFrom);
        workoutNotiEnablePref.setSummary(getString(isNotifEnabled));
        enableNotificationsPrefs(isNotifEnabled);

    }

    TimePickerDialog.OnTimeSetListener workoutTimeFrom = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            workoutFromC.set(Calendar.HOUR_OF_DAY,hourOfDay);
            workoutFromC.set(Calendar.MINUTE,minute);
            fromWorkoutTimePrf.setSummary(String.valueOf(hourOfDay)+":"+String.valueOf(minute));
            setTimePref(PreferenceKey.FROM_WORKOUT_KEY,String.valueOf(hourOfDay)+":"+String.valueOf(minute));
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

            case PreferenceKey.PREF_WORKOUT_IS_ENABLED:
                enableNotificationsPrefs(newValue);
                setSwitchPrefSummaries(newValue.toString(), preference);
                return true;

        }

        return true;
    }

    private void enableNotificationsPrefs(Object newValue) {

        if(newValue.equals(true))
        {
            fromWorkoutTimePrf.setEnabled(true);
        }
        else{
            fromWorkoutTimePrf.setEnabled(false);;
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

}