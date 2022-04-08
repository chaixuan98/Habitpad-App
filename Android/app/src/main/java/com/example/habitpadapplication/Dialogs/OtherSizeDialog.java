package com.example.habitpadapplication.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.habitpadapplication.DateHandler;
import com.example.habitpadapplication.DiaryActivity;
import com.example.habitpadapplication.HomeActivity;
import com.example.habitpadapplication.Model.WaterLog;
import com.example.habitpadapplication.R;
import com.example.habitpadapplication.SessionManager;
import com.example.habitpadapplication.Settings.PrefsHelper;
import com.example.habitpadapplication.Urls;
import com.example.habitpadapplication.VolleySingleton;
import com.example.habitpadapplication.WaterMainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Abeer on 4/30/2017.
 */

public class OtherSizeDialog extends Dialog implements View.OnClickListener,NumberPicker.OnValueChangeListener{
    private Context mContext;
    private List<WaterLog> waterLogs = new ArrayList<>();
    private NumberPicker numberPicker;
    private Button setButton;
    private int NumberPickerValue;

    private  MediaPlayer mediaPlayer;
    private final String OTHER= "other";

    private String userID;
    SessionManager sessionManager;


    public OtherSizeDialog(Context context, List<WaterLog> waterLogs) {
        super(context);
        this.mContext = context;
        this.waterLogs = waterLogs;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_picker_dialog);

        sessionManager = new SessionManager(mContext);
        sessionManager.checkLogin();
        HashMap<String,String> usersDetails = sessionManager.getUsersDetailFromSession();

        userID = usersDetails.get(SessionManager.KEY_USERID);

        numberPicker=(NumberPicker)findViewById(R.id.numberPicker);
        setButton = (Button) findViewById(R.id.set_button);
        mediaPlayer= MediaPlayer.create(mContext, R.raw.splash_water);
        setButton.setOnClickListener(this);
        numberPicker.setOnClickListener(this);

        setNumberPickerFormat();
        setTitle("Add drink");
    }


    private void setNumberPickerFormat() {
        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                value =value*5;
                return  value+" ml";
            }
        };
        numberPicker.setFormatter(formatter);
        numberPicker.setMaxValue(300);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(this);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.set_button:
                addFromNumberPiker();
        }


    }

    private void addFromNumberPiker() {

        addWater(NumberPickerValue,OTHER, DateHandler.getCurrentFormedDate(),DateHandler.getCurrentTime());
        playSound();
        dismiss();
    }

    private void playSound() {
        if (PrefsHelper.getSoundsPrefs(mContext))
            mediaPlayer.start();
    }

    private void SendUserToDiaryPage()
    {
        Intent addWaterIntent = new Intent(mContext, DiaryActivity.class);
        addWaterIntent.putExtra("intentFrom", "WaterOtherSizeDialog");
        addWaterIntent.putExtra("intentUserID", userID);
        mContext.startActivity(addWaterIntent);

    }


    private void addWater(int amount,String typ,String date,String time) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.WATER_TIME_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equals("OK")) {
                                Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
                                SendUserToDiaryPage();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(mContext,"Save Time Log Error!" + e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userID", userID);
                params.put("drunkWaterOnce", String.valueOf(amount));
                params.put("containerTyp", typ);
                params.put("waterDateTime", date);
                params.put("waterTime", time);
                return params;
            }
        };

        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
    }



    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        this.NumberPickerValue= picker.getValue()*5;
    }


}
