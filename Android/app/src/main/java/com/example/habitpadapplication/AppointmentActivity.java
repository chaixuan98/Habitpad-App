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
import com.example.habitpadapplication.Settings.WaterReminderActivity;
import com.example.habitpadapplication.Settings.WorkoutReminderActivity;

public class AppointmentActivity extends AppCompatActivity {

    private int[] Icons = {R.drawable.book_appointment, R.drawable.active_appointment, R.drawable.hisotry_appointment};

    private String[] Title = {"Book Appointment", "Active Appointment", "History"};

    private ListView appointmentList;
    private String intentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Appointment");
        setContentView(R.layout.activity_appointment);

        intentUserID = getIntent().getExtras().getString("intentUserID");

        appointmentList = (ListView) findViewById(R.id.appointment_list);
        AppointmentAdapter appointmentAdapter = new AppointmentAdapter();
        appointmentList.setAdapter(appointmentAdapter);

        appointmentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    //code specific to first list item
                    Intent appointmentIntent = new Intent(getApplicationContext(), DoctorListActivity.class);
                    appointmentIntent.putExtra("intentUserID", intentUserID);
                    startActivity(appointmentIntent);
                    //startActivity(new Intent(AppointmentActivity.this, DoctorListActivity.class));

                }

                else if(position == 1) {
                    //code specific to 2nd list item
                    Intent userAppointmentIntent = new Intent(getApplicationContext(), UserActiveAppointments.class);
                    userAppointmentIntent.putExtra("intentUserID", intentUserID);
                    startActivity(userAppointmentIntent);

                }

                else if(position == 2) {

                    startActivity(new Intent(AppointmentActivity.this, WaterReminderActivity.class));
                }
            }
        });
    }

    class AppointmentAdapter extends BaseAdapter {

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
            view = getLayoutInflater().inflate(R.layout.appointment_raw,null);

            ImageView iconView = view.findViewById(R.id.icon_view);
            TextView appointmentTV = view.findViewById(R.id.appointment_tv);

            iconView.setImageResource(Icons[position]);
            appointmentTV.setText(Title[position]);

            return view;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(AppointmentActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}