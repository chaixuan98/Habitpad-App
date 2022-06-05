package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyChallengeActivity extends AppCompatActivity {

    private ProgressBar mcProgress;
    private TextView mcTitle, mcTotalStep,mcStepRequired,mcStep,mcStartDate,mcEndDate,mcDesc;
    private ImageView mcPhoto;
    private  String userID, mchallengeID,mchallengePhoto,mchallengeTitle,mchallengeDesc,
            mchallengeStep,mchallengeStartDate,mchallengeEndDate,userChallengeTotalStep,userPoint, challengeAchievement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("My Challenge");
        setContentView(R.layout.activity_my_challenge);

        userID = getIntent().getExtras().getString("intentUserID");
        mchallengeID = getIntent().getExtras().getString("mchallengeID");

        mcPhoto = findViewById(R.id.mc_photo);
        mcTitle = findViewById(R.id.mc_title);
        mcStepRequired = findViewById(R.id.mc_step_required);
        mcStartDate = findViewById(R.id.mc_start_date);
        mcEndDate = findViewById(R.id.mc_end_date);
        mcDesc = findViewById(R.id.mc_description);

        mcTotalStep = findViewById(R.id.mc_total_step);
        mcStep = findViewById(R.id.mc_step);
        mcProgress = findViewById(R.id.mc_progress_bar);

        GetChallengeDetail(mchallengeID);
    }

    private void GetChallengeDetail(final String mchallengeID){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_CHALLENGE_DETAILS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("challengeDetail");
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                     mchallengePhoto = object.getString("challengePhoto").trim();
                                     mchallengeTitle = object.getString("challengeTitle").trim();
                                     mchallengeDesc = object.getString("challengeDescription").trim();
                                     mchallengeStep = object.getString("challengeStep").trim();
                                     mchallengeStartDate = object.getString("challengeStartDate").trim();
                                     mchallengeEndDate = object.getString("challengeEndDate").trim();


                                    Glide.with(MyChallengeActivity.this).asBitmap().load(mchallengePhoto)
                                            .fitCenter()
                                            .placeholder(R.drawable.ic_baseline_image_24)
                                            .error(R.drawable.ic_baseline_broken_image_24)
                                            .fallback(R.drawable.ic_baseline_image_not_supported_24)
                                            .dontAnimate().into(mcPhoto);
                                    mcTitle.setText(mchallengeTitle);
                                    mcDesc.setText(mchallengeDesc);
                                    mcStepRequired.setText(mchallengeStep);
                                    mcStep.setText(mchallengeStep);
                                    mcStartDate.setText(mchallengeStartDate);
                                    mcEndDate.setText(mchallengeEndDate);

                                    GetUserChallengeTotalStep(userID, mchallengeID);


                                }
                            }

                        }catch (Exception e){

                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MyChallengeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("challengeID", mchallengeID);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void GetUserChallengeTotalStep(final String userID, final String challengeID){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_CHALLENGE_TOTAL_STEP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("userChaStep");
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    userChallengeTotalStep = object.getString("userChallengeTotalStep").trim();
                                    challengeAchievement = object.getString("isChallegeAchieve").trim();

                                    mcTotalStep.setText(userChallengeTotalStep);

                                    Double setStepProgress=(Double.parseDouble(userChallengeTotalStep)*100/Double.parseDouble(mchallengeStep));
                                    int b=(int)Math.round(setStepProgress);
                                    mcProgress.setProgress(b);

                                    if(challengeAchievement.equals("false")) {
                                        if (Integer.parseInt(userChallengeTotalStep) >= Integer.parseInt(mchallengeStep)) {
                                            PopupAchievementDialog();
                                        }
                                    }

                                }
                            }

                        }catch (Exception e){

                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MyChallengeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID", userID);
                params.put("challengeID", challengeID);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(MyChallengeActivity.this, ChallengeActivity.class);
        intent.putExtra("intentUserID",userID);
        startActivity(intent);
        finish();
    }

    private void PopupAchievementDialog()
    {

        UpdateUserChallengeAchievement(userID);
        DisplayUserTotalPoint(userID);

        final Dialog achievementDialog = new Dialog(MyChallengeActivity.this);
        achievementDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        achievementDialog.setContentView(R.layout.achievements_layout);
        achievementDialog.setTitle("Achievement Window");
        achievementDialog.show();
        achievementDialog.setCanceledOnTouchOutside(false);
        achievementDialog.setCancelable(false);
        Window window = achievementDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView dialogDescription = achievementDialog.findViewById(R.id.achievement_dialog_description);
        dialogDescription.setText("Congratulations, You have complete the challenge.");

        TextView dialogPoints = achievementDialog.findViewById(R.id.achievement_dialog_points);
        dialogPoints.setText("+20 Points");

        ImageView cancelBtn = achievementDialog.findViewById(R.id.achievement_dialog_close_button);
        cancelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                achievementDialog.dismiss();
            }
        });
    }

    private void DisplayUserTotalPoint(final String intentUserID){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_TOTAL_POINT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("userPoint");
                            String success = jsonObject.getString("success") ;

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    userPoint = object.getString("totalPoint");

                                    userPoint = String.valueOf(Integer.parseInt(userPoint) + 20);
                                    UpdateUserPoint(userID, userPoint);
                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyChallengeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",intentUserID);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }

    private void UpdateUserPoint(final String intentUserID, final String point){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.UPDATE_USER_POINT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(MyChallengeActivity.this, "update user point error" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.i("tagerror", "["+error+"]");
                Toast.makeText(MyChallengeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",intentUserID);
                params.put("totalPoint",point);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void UpdateUserChallengeAchievement(final String intentUserID){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.UPDATE_USER_CHALLENGE_ACHIEVEMENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                //Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(MyChallengeActivity.this, "update user challenge achievement error" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.i("tagerror", "["+error+"]");
                Toast.makeText(MyChallengeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",intentUserID);
                params.put("challengeID",mchallengeID);
                params.put("isChallegeAchieve","true");
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);



    }

}