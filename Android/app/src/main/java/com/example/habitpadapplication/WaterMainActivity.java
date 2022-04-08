package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.example.habitpadapplication.Dialogs.OtherSizeDialog;
import com.example.habitpadapplication.Model.WaterLog;
import com.example.habitpadapplication.Settings.PrefsHelper;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaterMainActivity extends AppCompatActivity implements View.OnClickListener{

    private Context context;

    private List<WaterLog> waterLogs;

    private LinearLayout mainLayout;
    public static DonutProgress circleProgress;
    public static TextView choosenAmountTv;
    private BroadcastReceiver updateUIReciver;

    private final String GLASS="glass";
    private final String COFFEE="coffee";
    private final String TEA="tea";
    private final String COLA="cola";
    private final String JUICE="juice";

    private int glassSize;
    private int coffeeSize;
    private int teaSize;
    private int colaSize;
    private int juiceSize;

    private MediaPlayer mediaPlayer;

    private LinearLayout otherSize, glassButton,coffeeButton,teaButton,colaButton,juiceButton;

    private String intentUserID,intentUserGender,intentUserAge,intentUserLifestyle,intentUserWeight,intentUserHeight;;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_water_main);
        setTitle("Water Intake Tracker");

        context=getApplicationContext();

        Intent intent = getIntent();
        intentUserID = intent.getExtras().getString("intentUserID");
        intentUserGender = intent.getExtras().getString("intentUserGender");
        intentUserAge = intent.getExtras().getString("intentUserAge");
        intentUserLifestyle = intent.getExtras().getString("intentUserLifestyle");
        intentUserWeight = intent.getExtras().getString("intentUserWeight");
        intentUserHeight = intent.getExtras().getString("intentUserHeight");


        mediaPlayer= MediaPlayer.create(context, R.raw.splash_water);

        glassButton= (LinearLayout) findViewById(R.id.op50ml);
        coffeeButton= (LinearLayout) findViewById(R.id.op100ml);
        teaButton= (LinearLayout) findViewById(R.id.op150ml);
        colaButton= (LinearLayout) findViewById(R.id.op200ml);
        juiceButton= (LinearLayout) findViewById(R.id.op250ml);
        otherSize= (LinearLayout) findViewById(R.id.opCustom);


        glassButton.setOnClickListener(this);
        coffeeButton.setOnClickListener(this);
        teaButton.setOnClickListener(this);
        colaButton.setOnClickListener(this);
        juiceButton.setOnClickListener(this);
        otherSize.setOnClickListener(this);

        registerUIBroadcastReceiver();
        loadContainerSizePrefs();



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onDestroy(){
        unregisterReceiver(updateUIReciver);
        super.onDestroy();

    }

    private void registerUIBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.update.view.action");
        updateUIReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //updateView();
            }
        };
        registerReceiver(updateUIReciver,filter);
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.op50ml:
                addGlass();
                break;
            case R.id.op100ml:
                addCoffee();
                break;
            case R.id.op150ml:
                addTea();
                break;
            case R.id.op200ml:
                addCola();
                break;
            case R.id.op250ml:
                addJuice();
                break;
            case R.id.opCustom:
                showOtherSizeDialog();
                break;


        }

    }


    private void showOtherSizeDialog() {
        OtherSizeDialog otherSizeDialog = new OtherSizeDialog(this, waterLogs);
        otherSizeDialog.show();
    }

    private void playSound() {
        if (PrefsHelper.getSoundsPrefs(context))
            mediaPlayer.start();
    }

    private void addGlass(){
        addWater(glassSize,GLASS, DateHandler.getCurrentFormedDate(),DateHandler.getCurrentTime());
        playSound();
    }

    private void addCoffee(){
        addWater(coffeeSize,COFFEE, DateHandler.getCurrentFormedDate(),DateHandler.getCurrentTime());
        playSound();
    }
    private void addTea(){
        addWater(teaSize,TEA, DateHandler.getCurrentFormedDate(),DateHandler.getCurrentTime());
        playSound();
    }
    private void addCola(){
        addWater(colaSize,COLA, DateHandler.getCurrentFormedDate(),DateHandler.getCurrentTime());
        playSound();
    }
    private void addJuice(){
        addWater(juiceSize,JUICE, DateHandler.getCurrentFormedDate(),DateHandler.getCurrentTime());
        playSound();
    }



    private void loadContainerSizePrefs() {
        String glassSizeStr= PrefsHelper.getGlassSizePrefs(context);
        String coffeeSizeStr= PrefsHelper.getCoffeeSizePrefs(context);
        String teaSizeStr= PrefsHelper.getTeaSizePrefs(context);
        String colaSizeStr= PrefsHelper.getColaSizePrefs(context);
        String juiceSizeStr =PrefsHelper.getJuiceSizePrefs(context);
        this.glassSize = Integer.valueOf(glassSizeStr);
        this.coffeeSize = Integer.valueOf(coffeeSizeStr);
        this.teaSize = Integer.valueOf(teaSizeStr);
        this.colaSize = Integer.valueOf(colaSizeStr);
        this.juiceSize = Integer.valueOf(juiceSizeStr);

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
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                                SendUserToDiaryPage();

                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"Save Water Error!" + e.toString(),Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userID", intentUserID);
                params.put("drunkWaterOnce", String.valueOf(amount));
                params.put("containerTyp", typ);
                params.put("waterDateTime", date);
                params.put("waterTime", time);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void SendUserToDiaryPage()
    {
        Intent addWaterIntent = new Intent(context, DiaryActivity.class);
        addWaterIntent.putExtra("intentFrom", "Water");
        addWaterIntent.putExtra("intentUserID", intentUserID);
        addWaterIntent.putExtra("intentUserGender", intentUserGender);
        addWaterIntent.putExtra("intentUserAge", intentUserAge);
        addWaterIntent.putExtra("intentUserLifestyle", intentUserLifestyle);
        addWaterIntent.putExtra("intentUserWeight", intentUserWeight);
        addWaterIntent.putExtra("intentUserHeight", intentUserHeight);
        startActivity(addWaterIntent);

    }

}