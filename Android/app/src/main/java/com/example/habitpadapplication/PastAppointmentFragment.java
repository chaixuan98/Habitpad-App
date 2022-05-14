package com.example.habitpadapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.habitpadapplication.Adapters.UserAppointmetAdapter;
import com.example.habitpadapplication.Model.UserAppointment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PastAppointmentFragment extends Fragment {

    private RecyclerView pastAppointmentRecyclerView;
    private RecyclerView.LayoutManager manager;
    private UserAppointmetAdapter userAppAdapter;
    private List<UserAppointment> userAppointments;

    private String intentUserID;
    private TextView noAppTV;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_past_appointment, container, false);



        Intent intent = ((Activity) getContext()).getIntent();
        intentUserID = intent.getExtras().getString("intentUserID");

        pastAppointmentRecyclerView = view.findViewById(R.id.user_past_appointments_rv);
        noAppTV = view.findViewById(R.id.past_app_tv);

        manager = new LinearLayoutManager(getContext());
        pastAppointmentRecyclerView.setLayoutManager(manager);
        pastAppointmentRecyclerView.setHasFixedSize(true);
        userAppointments = new ArrayList<>();
        userAppAdapter = new UserAppointmetAdapter(getContext(), userAppointments);

        getUserAppointment();

        return view;
    }

    private void getUserAppointment() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        calendar = Calendar.getInstance();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_APPOINTMENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("userapp");

                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String appointmentID = object.getString("appointmentID").trim();
                                    String doctorID = object.getString("doctorID").trim();
                                    String doctorPhoto = object.getString("doctorPhoto").trim();
                                    String doctorName = object.getString("doctorName").trim();
                                    String appointmentDate = object.getString("appointmentDate").trim();
                                    String appointmentTime = object.getString("appointmentTime").trim();
                                    String appointmentRemark = object.getString("appointmentRemark").trim();

                                    dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    date = dateFormat.format(calendar.getTime());

                                    noAppTV.setVisibility(View.GONE);

                                    if(appointmentDate.compareTo(date) < 0 ){
                                        //Log.i("app", "Date1 is after Date2");
                                        UserAppointment userAppointment = new UserAppointment(appointmentID,doctorID,doctorPhoto, doctorName, appointmentDate, appointmentTime, appointmentRemark);
                                        userAppointments.add(userAppointment);
                                        pastAppointmentRecyclerView.setAdapter(userAppAdapter);
                                    }

                                    if (userAppAdapter.getItemCount() == 0)
                                    {
                                        noAppTV.setVisibility(View.VISIBLE);
                                    }



                                }
                            }

                        }catch (Exception e){
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userID", intentUserID);
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }
}