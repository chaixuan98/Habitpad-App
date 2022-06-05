package com.example.habitpadapplication.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.habitpadapplication.AppointmentActivity;
import com.example.habitpadapplication.DateHandler;
import com.example.habitpadapplication.DoctorListActivity;
import com.example.habitpadapplication.FeedbackActivity;
import com.example.habitpadapplication.HomeActivity;
import com.example.habitpadapplication.MissionRewardActivity;
import com.example.habitpadapplication.R;
import com.example.habitpadapplication.Urls;
import com.example.habitpadapplication.UserAppointments;
import com.example.habitpadapplication.ViewTipActivity;
import com.example.habitpadapplication.VolleySingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccountSettingActivity extends AppCompatActivity {
    private int[] Icons = {R.drawable.reward, R.drawable.edit_profile,R.drawable.feedback};

    private String[] Title = {"Mission & Rewards", "Edit Profile", "Feedback"};

    private ListView settingList;
    private String userID;

    private TextView settingUsername, settingUserEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Account");
        setContentView(R.layout.activity_account_setting);

        userID = getIntent().getExtras().getString("intentUserID");


        settingList = (ListView) findViewById(R.id.setting_list);
        AccountSettingActivity.SettingAdapter settingAdapter = new AccountSettingActivity.SettingAdapter();
        settingList.setAdapter(settingAdapter);

        settingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    //code specific to first list item
                    Intent missionRewardIntent = new Intent(getApplicationContext(), MissionRewardActivity.class);
                    missionRewardIntent.putExtra("intentUserID", userID);
                    startActivity(missionRewardIntent);
                    //startActivity(new Intent(AppointmentActivity.this, DoctorListActivity.class));

                }

                else if(position == 1) {
                    //code specific to 2nd list item
                    Intent userAppointmentIntent = new Intent(getApplicationContext(), UserSettingsActivity.class);
                    userAppointmentIntent.putExtra("intentUserID", userID);
                    startActivity(userAppointmentIntent);

                }

                else if(position == 2) {
                    //code specific to 3rd list item
                    Intent feedbackIntent = new Intent(getApplicationContext(), FeedbackActivity.class);
                    feedbackIntent.putExtra("intentUserID", userID);
                    startActivity(feedbackIntent);

                }

            }
        });

    }

    class SettingAdapter extends BaseAdapter {

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
            view = getLayoutInflater().inflate(R.layout.setting_raw,null);

            ImageView iconView = view.findViewById(R.id.setting_icon_view);
            TextView settingTV = view.findViewById(R.id.setting_tv);

            iconView.setImageResource(Icons[position]);
            settingTV.setText(Title[position]);

            return view;
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(AccountSettingActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}