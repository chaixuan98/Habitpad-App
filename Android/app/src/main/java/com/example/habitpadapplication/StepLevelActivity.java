package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StepLevelActivity extends AppCompatActivity {

    private ImageView backarrow,levelimg,three,seven,ten,fourt,twenty,thirty,fourty,sixty;
    private TextView stepsleft,leveltext,imgtext;
    private int totalsteps=0,goal=3000;
    public static NumberFormat formatter=NumberFormat.getInstance(Locale.getDefault());
    private ProgressBar progressBar;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Step Level");
        setContentView(R.layout.activity_step_level);

        userID = getIntent().getExtras().getString("intentUserID");

        stepsleft=findViewById(R.id.stepslefttonextlevel);
        leveltext=findViewById(R.id.levtext);
        imgtext=findViewById(R.id.mainacheivemnttext);
        progressBar=findViewById(R.id.progressBar);
        levelimg=findViewById(R.id.mainacheivemnt);
        three=findViewById(R.id.three);
        ten=findViewById(R.id.ten);
        seven=findViewById(R.id.seven);
        fourt=findViewById(R.id.fourteen);
        twenty=findViewById(R.id.twenty);
        thirty=findViewById(R.id.thirty);
        fourty=findViewById(R.id.fourty);
        sixty=findViewById(R.id.sixty);

        loadtotalsteps();
        //loadimages();
        levelimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogShow();
            }

        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(StepLevelActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadtotalsteps() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_TOTAL_STEP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("step", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("uTotalStep");
                            String success = jsonObject.getString("success") ;

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    totalsteps = object.getInt("userTotalStep");
                                    loadimages();
                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StepLevelActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",userID);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void loadimages(){
        if(totalsteps<3000){
            levelimg.setBackgroundColor(Color.GRAY);
            levelimg.setImageResource(R.drawable.editthree);
            goal=3000;
            stepsleft.setText(formatter.format(3000-totalsteps));
            leveltext.setText("1");

        }
        if(totalsteps>=3000 && totalsteps<7000){
            levelimg.setBackgroundColor(Color.BLUE);
            three.setBackgroundColor(Color.BLUE);
            levelimg.setImageResource(R.drawable.editthree);
            goal=7000;
            stepsleft.setText(formatter.format(7000-totalsteps));
            leveltext.setText("2");

        }
        if(totalsteps>=7000 && totalsteps<10000){
            levelimg.setBackgroundColor(Color.BLUE);
            levelimg.setImageResource(R.drawable.editseven);
            goal=10000;
            three.setBackgroundColor(Color.BLUE);
            seven.setBackgroundColor(Color.BLUE);
            stepsleft.setText(formatter.format(10000-totalsteps));
            leveltext.setText("3");
            imgtext.setText("Keep Fit");
        }
        if(totalsteps>=10000 && totalsteps<14000){
            levelimg.setBackgroundColor(Color.BLUE);
            levelimg.setImageResource(R.drawable.edit10);
            goal=14000;
            three.setBackgroundColor(Color.BLUE);
            seven.setBackgroundColor(Color.BLUE);
            ten.setBackgroundColor(Color.BLUE);
            stepsleft.setText(formatter.format(14000-totalsteps));
            leveltext.setText("4");
            imgtext.setText("Healthy Day");
        }
        if(totalsteps>=14000 && totalsteps<20000){
            levelimg.setBackgroundColor(Color.BLUE);
            levelimg.setImageResource(R.drawable.editfort);
            goal=20000;
            stepsleft.setText(formatter.format(20000-totalsteps));
            leveltext.setText("5");
            three.setBackgroundColor(Color.BLUE);
            seven.setBackgroundColor(Color.BLUE);
            ten.setBackgroundColor(Color.BLUE);
            fourt.setBackgroundColor(Color.BLUE);
            imgtext.setText("Slimmer Day");
        }
        if(totalsteps>=20000 && totalsteps<30000){
            levelimg.setBackgroundColor(Color.BLUE);
            levelimg.setImageResource(R.drawable.edittwenty);
            goal=30000;
            stepsleft.setText(formatter.format(30000-totalsteps));
            leveltext.setText("6");
            three.setBackgroundColor(Color.BLUE);
            seven.setBackgroundColor(Color.BLUE);
            ten.setBackgroundColor(Color.BLUE);
            fourt.setBackgroundColor(Color.BLUE);
            twenty.setBackgroundColor(Color.BLUE);
            imgtext.setText("Hiker");
        }
        if(totalsteps>=30000 && totalsteps<40000){
            levelimg.setBackgroundColor(Color.BLUE);
            levelimg.setImageResource(R.drawable.editthirty);
            goal=40000;
            stepsleft.setText(formatter.format(40000-totalsteps));
            leveltext.setText("7");
            three.setBackgroundColor(Color.BLUE);
            seven.setBackgroundColor(Color.BLUE);
            ten.setBackgroundColor(Color.BLUE);
            fourt.setBackgroundColor(Color.BLUE);
            imgtext.setText("Explorer");
        }
        if(totalsteps>=40000 && totalsteps<60000){
            levelimg.setBackgroundColor(Color.BLUE);
            levelimg.setImageResource(R.drawable.editforty);
            goal=60000;
            stepsleft.setText(formatter.format(60000-totalsteps));
            leveltext.setText("8");
            three.setBackgroundColor(Color.BLUE);
            seven.setBackgroundColor(Color.BLUE);
            ten.setBackgroundColor(Color.BLUE);
            fourt.setBackgroundColor(Color.BLUE);
            imgtext.setText("Hero");
        }
        if(totalsteps>=60000 && totalsteps<70000){
            levelimg.setBackgroundColor(Color.BLUE);
            levelimg.setImageResource(R.drawable.editforty);
            goal=700000;
            stepsleft.setText(formatter.format(70000-totalsteps));
            leveltext.setText("9");
            three.setBackgroundColor(Color.BLUE);
            seven.setBackgroundColor(Color.BLUE);
            ten.setBackgroundColor(Color.BLUE);
            fourt.setBackgroundColor(Color.BLUE);
            imgtext.setText("Conqueror");
        }
        float set=((totalsteps*100)/goal);
        int b=(int)Math.round(set);
        progressBar.setProgress(b);

    }

    protected void DialogShow(){
        final Dialog stepLevelDialog = new Dialog(StepLevelActivity.this);
        stepLevelDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        stepLevelDialog.setContentView(R.layout.achievements_layout);
        stepLevelDialog.setTitle("Achievement Window");
        stepLevelDialog.show();
        stepLevelDialog.setCanceledOnTouchOutside(false);
        stepLevelDialog.setCancelable(false);
        Window window = stepLevelDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

//        final Dialog dialog=new Dialog(getApplicationContext());
//        dialog.setCancelable(true);
//        View view=getApplicationContext().getLayoutInflater().inflate(com.example.pedometer.R.layout.acheivementdialog,null);
//        dialog.setContentView(view);

        loadtotalsteps();
        ImageView levelimg=stepLevelDialog.findViewById(R.id.imageview);
        TextView leveltext=stepLevelDialog.findViewById(R.id.textlevel);
        if(totalsteps<3000){
            levelimg.setBackgroundColor(Color.GRAY);
            levelimg.setImageResource(R.drawable.editthree);
            leveltext.setText("1");

        }
        if(totalsteps>=3000 && totalsteps<7000){
            levelimg.setBackgroundColor(Color.BLUE);

            levelimg.setImageResource(R.drawable.editthree);

            leveltext.setText("2");

        }
        if(totalsteps>=7000 && totalsteps<10000){
            levelimg.setBackgroundColor(Color.BLUE);
            levelimg.setImageResource(R.drawable.editseven);

            leveltext.setText("3");
        }
        if(totalsteps>=10000 && totalsteps<14000){
            levelimg.setBackgroundColor(Color.BLUE);
            levelimg.setImageResource(R.drawable.edit10);

            leveltext.setText("4");
        }
        if(totalsteps>=14000 && totalsteps<20000){
            levelimg.setBackgroundColor(Color.BLUE);
            levelimg.setImageResource(R.drawable.editfort);

            leveltext.setText("5");

        }
        if(totalsteps>=20000 && totalsteps<30000){
            levelimg.setBackgroundColor(Color.BLUE);
            levelimg.setImageResource(R.drawable.edittwenty);

            leveltext.setText("6");

        }
        if(totalsteps>=30000 && totalsteps<40000){
            levelimg.setBackgroundColor(Color.BLUE);
            levelimg.setImageResource(R.drawable.editthirty);

            leveltext.setText("7");

        }
        if(totalsteps>=40000 && totalsteps<60000){
            levelimg.setBackgroundColor(Color.BLUE);
            levelimg.setImageResource(R.drawable.editforty);

            leveltext.setText("8");

        }
        if(totalsteps>=60000 && totalsteps<70000){
            levelimg.setBackgroundColor(Color.BLUE);
            levelimg.setImageResource(R.drawable.editforty);

            leveltext.setText("9");
        }
        Button close=stepLevelDialog.findViewById(R.id.ok);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepLevelDialog.dismiss();
            }
        });
        stepLevelDialog.show();
    }
}