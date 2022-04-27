package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.habitpadapplication.Settings.FoodReminderActivity;
import com.example.habitpadapplication.Settings.PrefsHelper;
import com.example.habitpadapplication.Settings.WaterReminderActivity;
import com.example.habitpadapplication.Settings.WorkoutReminderActivity;

public class ReminderActivity extends AppCompatActivity {

    private int[] Icons = {R.drawable.diet_icon, R.drawable.workout_img, R.drawable.water_icon};

    private String[] Title = {"Food Reminder", "Workout Reminder", "Water Intake Reminder"};

    private ListView reminderList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Reminder");
        setContentView(R.layout.activity_reminder);


        reminderList = (ListView) findViewById(R.id.reminder_list);
        ReminderAdapter reminderAdapter = new ReminderAdapter();
        reminderList.setAdapter(reminderAdapter);



        reminderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    //code specific to first list item
                    startActivityForResult(new Intent(ReminderActivity.this, FoodReminderActivity.class),0);
//                    startActivity(new Intent(ReminderActivity.this, FoodReminderActivity.class));
//                    loadFoodNotificationsPrefs();

                }

                else if(position == 1) {
                    //code specific to 2nd list item
                    startActivityForResult(new Intent(ReminderActivity.this, WorkoutReminderActivity.class),1);
                    //Toast.makeText(getApplicationContext(),"Place Your Second Option Code",Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(ReminderActivity.this, WorkoutReminderActivity.class));
//                    loadWorkoutNotificationsPrefs();
                }

                else if(position == 2) {

                    startActivityForResult(new Intent(ReminderActivity.this, WaterReminderActivity.class),2);
//                    startActivity(new Intent(ReminderActivity.this, WaterReminderActivity.class));
//                    loadNotificationsPrefs();
                }
            }
        });
    }

    class ReminderAdapter extends BaseAdapter{

        private int position;
        private View view;
        private ViewGroup parent;

        @Override
        public int getCount() {
            return Icons.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.reminder_raw,null);

            ImageView iconView = view.findViewById(R.id.icon_view);
            TextView reminderTV = view.findViewById(R.id.reminder_tv);

            iconView.setImageResource(Icons[position]);
            reminderTV.setText(Title[position]);

            return view;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        switch (requestCode) {
            case 0:
                loadFoodNotificationsPrefs();
                loadFoodLunchNotificationsPrefs();
                loadFoodDinnerNotificationsPrefs();
                loadFoodSnackNotificationsPrefs();
                break;

            case 1:
                loadWorkoutNotificationsPrefs();
                break;

            case 2:
                loadNotificationsPrefs();
                break;
        }

    }

    private void loadNotificationsPrefs() {
        boolean isEnable= PrefsHelper.getNotificationsPrefs(getApplicationContext());


        if (isEnable)
        {
            AlarmHelper.setNotificationsAlarm(getApplicationContext());
            AlarmHelper.setCancelNotificationAlarm(getApplicationContext());
        }
        else
        {
            AlarmHelper.stopNotificationsAlarm(getApplicationContext());
        }

    }

    private void loadFoodNotificationsPrefs() {

        boolean isFoodEnable= PrefsHelper.getFoodNotificationsPrefs(getApplicationContext());

        if (isFoodEnable )
        {
            AlarmHelper.setFoodNotificationsAlarm(getApplicationContext());
        }

        else
        {
            AlarmHelper.stopNotificationsAlarm(getApplicationContext());
        }

    }

    private void loadFoodLunchNotificationsPrefs() {

        boolean isFoodLunchEnable= PrefsHelper.getFoodLunchNotificationsPrefs(getApplicationContext());

        if (isFoodLunchEnable )
        {
            AlarmHelper.setFoodLunchNotificationsAlarm(getApplicationContext());
        }
        else
        {
            AlarmHelper.stopNotificationsAlarm(getApplicationContext());
        }

    }

    private void loadFoodDinnerNotificationsPrefs() {

        boolean isFoodDinnerEnable= PrefsHelper.getFoodDinnerNotificationsPrefs(getApplicationContext());

        if (isFoodDinnerEnable )
        {
            AlarmHelper.setFoodDinnerNotificationsAlarm(getApplicationContext());
        }

        else
        {
            AlarmHelper.stopNotificationsAlarm(getApplicationContext());
        }

    }

    private void loadFoodSnackNotificationsPrefs() {

        boolean isFoodSnackEnable= PrefsHelper.getFoodSnackNotificationsPrefs(getApplicationContext());


        if (isFoodSnackEnable )
        {
            AlarmHelper.setFoodSnackNotificationsAlarm(getApplicationContext());
        }

        else
        {
            AlarmHelper.stopNotificationsAlarm(getApplicationContext());
        }

    }

    private void loadWorkoutNotificationsPrefs() {

        boolean isWorkoutEnable= PrefsHelper.getWorkoutNotificationsPrefs(getApplicationContext());

        if (isWorkoutEnable )
        {
            AlarmHelper.setWorkoutNotificationsAlarm(getApplicationContext());
        }

        else
        {
            AlarmHelper.stopNotificationsAlarm(getApplicationContext());
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(ReminderActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}