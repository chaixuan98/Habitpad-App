package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.habitpadapplication.Dialogs.OtherSizeDialog;
import com.example.habitpadapplication.Dialogs.TermConditionDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorMainActivity extends AppCompatActivity {

    // Variable declarations
    DoctorSessionManager doctorSessionManager;

    DrawerLayout drawerLayout;
    CardView my_schedule, my_app;
    TextView nav_name;
    CircleImageView image;

    private Context context;
    private String doctorID, doctorName, doctorEmail, doctorPhone, doctorEducation, doctorExp, doctorHospital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        context=getApplicationContext();

        doctorSessionManager = new DoctorSessionManager(context);
        doctorSessionManager.checkLogin();
        HashMap<String,String> doctorDetails = doctorSessionManager.getDoctorDetailFromSession();

        doctorID = doctorDetails.get(DoctorSessionManager.KEY_DOCTORID);
        doctorName = doctorDetails.get(DoctorSessionManager.KEY_DOCTORNAME);


        drawerLayout = findViewById(R.id.doctor_drawer_layout);
        nav_name = findViewById(R.id.nav_name);
        nav_name.setText("Dr."+doctorName);
        image = findViewById(R.id.nav_image);
        image.setImageResource(R.drawable.doctor_avatar_white);

        my_schedule = findViewById(R.id.my_schedule);
        my_app = findViewById(R.id.my_app);

        DisplayDoctorProfilePic(doctorID);

        checkAppFirstTimeRun();

        my_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorMainActivity.this, DoctorSchedule.class);
                intent.putExtra("doctorID", doctorID);
                startActivity(intent);
            }
        });

        my_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorMainActivity.this, DoctorAppointments.class);
                intent.putExtra("doctorID", doctorID);
                startActivity(intent);
            }
        });
    }

    private void termDialog(){
        TermConditionDialog termConditionDialog = new TermConditionDialog(this);
        termConditionDialog.show();
    }

    private void checkAppFirstTimeRun() {

        if (doctorSessionManager.getFirstTimeRunPrefs()) {
            termDialog();
            doctorSessionManager.setFirstTimeRunPrefs(false);

        }

    }

    public void ClickMenu(View view) {
        openDrawer(drawerLayout);
    }

    static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view) {
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void ClickHome(View view)  {
        recreate();
    }

    public void ClickUpdateProfile(View view) {

        Intent profileIntent = new Intent(this, DoctorUpdateProfile.class);
        profileIntent.putExtra("intentDoctorID", doctorID);
        profileIntent.putExtra("intentDoctorName", doctorName);
        profileIntent.putExtra("intentDoctorEmail", doctorEmail);
        profileIntent.putExtra("intentDoctorPhone", doctorPhone);
        profileIntent.putExtra("intentDoctorEducation", doctorEducation);
        profileIntent.putExtra("intentDoctorExp", doctorExp);
        profileIntent.putExtra("intentDoctorHospital", doctorHospital);

        startActivity(profileIntent);
    }

    public void ClickLogout(View view) {
        logout(this);
    }

    public void logout(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to Logout ?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                doctorSessionManager.logoutDoctorFromSession();

                SharedPreferences sp = getSharedPreferences("Shpr", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();
                ed.clear();
                ed.commit();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

//    public static void redirectActivity(Activity activity, Class aclass) {
//        Intent intent = new Intent(activity, aclass);
//        intent.putExtra("UserName", doctorName);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        activity.startActivity(intent);
//    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    private void DisplayDoctorProfilePic(final String doctorID){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_DOCTOR_PROFILE_PIC_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("doctorpic");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String doctorPic= object.getString("doctorPhoto");

                                    Glide.with(DoctorMainActivity.this).asBitmap().load(doctorPic)
                                            .fitCenter()
                                            .dontAnimate().into(image);

                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DoctorMainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("doctorID",doctorID);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);



    }
}