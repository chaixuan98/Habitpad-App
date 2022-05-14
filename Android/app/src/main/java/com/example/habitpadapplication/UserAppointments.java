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


    private RecyclerView activeAppointmentRecyclerView;
    private RecyclerView.LayoutManager manager;
    private UserAppointmetAdapter userAppAdapter;
    private List<UserAppointment> userAppointments;

    private Context context;
    private String intentUserID;

    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Active Appointment");
        setContentView(R.layout.activity_user_appointments);

        tabLayout = findViewById(R.id.appointment_tab_layout);
        viewPager = findViewById(R.id.appointment_view_pager);

        tabLayout.setupWithViewPager(viewPager);

        AppointmentViewPagerAdapter appointmentViewPagerAdapter = new AppointmentViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        appointmentViewPagerAdapter.addFragment(new ActiveAppointmentFragment(), "Active");
        appointmentViewPagerAdapter.addFragment(new PastAppointmentFragment(), "Past");
        viewPager.setAdapter(appointmentViewPagerAdapter);

//        activeAppointmentRecyclerView = findViewById(R.id.user_appointments_rv);
//
//        intentUserID = getIntent().getExtras().getString("intentUserID");
//
//        manager = new LinearLayoutManager(UserAppointments.this);
//        activeAppointmentRecyclerView.setLayoutManager(manager);
//        activeAppointmentRecyclerView.setHasFixedSize(true);
//        userAppointments = new ArrayList<>();
//        userAppAdapter = new UserAppointmetAdapter(UserAppointments.this, userAppointments);
//
//        getUserAppointment();
    }

//    private void getUserAppointment() {
//        // Initializing Request queue
//
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_APPOINTMENT_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        progressDialog.dismiss();
//                        try {
//                            Log.i("tagconvertstr", "["+response+"]");
//                            //JSONArray array = new JSONArray(response);
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONArray jsonArray = jsonObject.getJSONArray("userapp");
//
//                            String success = jsonObject.getString("success");
//
//                            if (success.equals("1")) {
//
//                                for (int i = 0; i < jsonArray.length(); i++) {
//
//                                    JSONObject object = jsonArray.getJSONObject(i);
//
//                                    String appointmentID = object.getString("appointmentID").trim();
//                                    String doctorID = object.getString("doctorID").trim();
//                                    String doctorPhoto = object.getString("doctorPhoto").trim();
//                                    String doctorName = object.getString("doctorName").trim();
//                                    String appointmentDate = object.getString("appointmentDate").trim();
//                                    String appointmentTime = object.getString("appointmentTime").trim();
//                                    String appointmentRemark = object.getString("appointmentRemark").trim();
//
//
//                                    UserAppointment userAppointment = new UserAppointment(appointmentID,doctorID,doctorPhoto, doctorName, appointmentDate, appointmentTime, appointmentRemark);
//                                    userAppointments.add(userAppointment);
//                                }
//                            }
//
//                        }catch (Exception e){
//                            progressDialog.dismiss();
//                            e.printStackTrace();
//                        }
//
//                        activeAppointmentRecyclerView.setAdapter(userAppAdapter);
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
//                Toast.makeText(UserAppointments.this, error.toString(), Toast.LENGTH_LONG).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("userID", intentUserID);
//                return params;
//            }
//        };
//
//        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
//
//    }

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