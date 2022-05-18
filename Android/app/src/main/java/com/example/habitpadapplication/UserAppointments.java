package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.habitpadapplication.Adapters.AppointmentViewPagerAdapter;
import com.example.habitpadapplication.Adapters.UserAppointmetAdapter;
import com.example.habitpadapplication.Model.UserAppointment;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAppointments extends AppCompatActivity {

    private String intentUserID;

    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Appointments");
        setContentView(R.layout.activity_user_appointments);

        intentUserID = getIntent().getExtras().getString("intentUserID");

        tabLayout = findViewById(R.id.appointment_tab_layout);
        viewPager = findViewById(R.id.appointment_view_pager);

        tabLayout.setupWithViewPager(viewPager);

        AppointmentViewPagerAdapter appointmentViewPagerAdapter = new AppointmentViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        appointmentViewPagerAdapter.addFragment(new ActiveAppointmentFragment(), "Active");
        appointmentViewPagerAdapter.addFragment(new PastAppointmentFragment(), "Past");
        viewPager.setAdapter(appointmentViewPagerAdapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(UserAppointments.this, AppointmentActivity.class);
        intent.putExtra("intentUserID", intentUserID);
        startActivity(intent);
        finish();
    }
}