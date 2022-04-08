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

public class FragmentPrefs extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener{

    private WaterReminderActivity mActivity;
    private Context context;
    private Preference intervalPref, soundPref, notiEnablePref,fromTimePrf, toTimePref;
    private Calendar fromC = Calendar.getInstance();
    private Calendar toC = Calendar.getInstance();


    //variables
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public FragmentPrefs(Context _context){
        context = _context;
        sharedPreferences = _context.getSharedPreferences("userWaterReminderSession",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }



    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context =mActivity.getApplicationContext();
        addPreferencesFromResource(R.xml.fragment_prefs);


        notiEnablePref= findPreference(PreferenceKey.PREF_IS_ENABLED);
        soundPref= findPreference(PreferenceKey.PREF_SOUND);
        intervalPref=findPreference(PreferenceKey.PREF_INTERVAL);
        fromTimePrf =  findPreference(PreferenceKey.PREF_START_TIME);
        toTimePref= findPreference(PreferenceKey.PREF_EBD_TIME);



        toTimePref.setOnPreferenceChangeListener(this);
        notiEnablePref.setOnPreferenceChangeListener(this);
        intervalPref.setOnPreferenceChangeListener(this);
        soundPref.setOnPreferenceChangeListener(this);

        initSummaries();



        fromTimePrf.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {
                int hour = fromC.get(Calendar.HOUR_OF_DAY);
                int minutes = fromC.get(Calendar.MINUTE);
                TimePickerDialog datePickerDialog =
                        new TimePickerDialog(getActivity(),timeFrom ,hour,minutes, true);
                datePickerDialog.show();
                return false;
            }
        });

        toTimePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {
                int hour = toC.get(Calendar.HOUR_OF_DAY);
                int minutes = toC.get(Calendar.MINUTE);
                TimePickerDialog datePickerDialog =
                        new TimePickerDialog(getActivity(),timeTo ,hour,minutes, true);
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
            mActivity=(WaterReminderActivity)activity;
        }

    }

    private void initSummaries() {
        boolean isNotifEnabled = getPreferenceManager().getSharedPreferences().getBoolean(PreferenceKey.PREF_IS_ENABLED, false);
        boolean isSoundEnabled = getPreferenceManager().getSharedPreferences().getBoolean(PreferenceKey.PREF_SOUND, false);
        int intervalNum = getPreferenceManager().getSharedPreferences().getInt(PreferenceKey.PREF_INTERVAL,2);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String from = sharedPreferences.getString(PreferenceKey.FROM_KEY,"8:0");
        String to = sharedPreferences.getString(PreferenceKey.TO_KEY,"20:0");

        fromTimePrf.setSummary(from);
        toTimePref.setSummary(to);
        notiEnablePref.setSummary(getString(isNotifEnabled));
        enableNotificationsPrefs(isNotifEnabled);
        soundPref.setSummary(getString(isSoundEnabled));
        intervalPref.setSummary(Integer.toString(intervalNum)+" hour/s");

    }

    TimePickerDialog.OnTimeSetListener timeFrom = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            fromC.set(Calendar.HOUR_OF_DAY,hourOfDay);
            fromC.set(Calendar.MINUTE,minute);
            fromTimePrf.setSummary(String.valueOf(hourOfDay)+":"+String.valueOf(minute));
            setTimePref(PreferenceKey.FROM_KEY,String.valueOf(hourOfDay)+":"+String.valueOf(minute));
        }
    };

    TimePickerDialog.OnTimeSetListener timeTo = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            toC.set(Calendar.HOUR_OF_DAY,hourOfDay);
            toC.set(Calendar.MINUTE,minute);
            toTimePref.setSummary(String.valueOf(hourOfDay)+":"+String.valueOf(minute));
            setTimePref(PreferenceKey.TO_KEY,String.valueOf(hourOfDay)+":"+String.valueOf(minute));
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

            case PreferenceKey.PREF_IS_ENABLED:
                enableNotificationsPrefs(newValue);
                setSwitchPrefSummaries(newValue.toString(), preference);
                return true;

            case PreferenceKey.PREF_SOUND:
                setSwitchPrefSummaries(newValue.toString(), preference);
                return true;

            case PreferenceKey.PREF_INTERVAL:
                preference.setSummary(newValue.toString()+" hour/s");
                return true;

        }

        return true;
    }

    private void enableNotificationsPrefs(Object newValue) {

        if(newValue.equals(true))
        {
            intervalPref.setEnabled(true);
            fromTimePrf.setEnabled(true);
            toTimePref.setEnabled(true);

        }
        else{
            intervalPref.setEnabled(false);
            fromTimePrf.setEnabled(false);
            toTimePref.setEnabled(false);
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

    public void logoutUserFromWaterReminderSession(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().clear().commit();
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.clear();
//        editor.commit();

    }

}