
package com.example.pedometer.Components;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pedometer.BuildConfig;
import com.example.pedometer.Database.Database;
import com.example.pedometer.util.Logger;
import com.example.pedometer.util.StepUrls;
import com.example.pedometer.util.Util;
import com.example.pedometer.util.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ShutdownReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (BuildConfig.DEBUG) Logger.log("shutting down");

        context.startService(new Intent(context, SensorListener.class));

        // if the user used a root script for shutdown, the DEVICE_SHUTDOWN
        // broadcast might not be send. Therefore, the app will check this
        // setting on the next boot and displays an error message if it's not
        // set to true
        context.getSharedPreferences("pedometer", Context.MODE_PRIVATE).edit()
                .putBoolean("correctShutdown", true).commit();

        Database db = Database.getInstance(context);
        // if it's already a new day, add the temp. steps to the last one
        if (db.getSteps(Util.getToday()) == Integer.MIN_VALUE) {
            int steps = db.getCurrentSteps();
            db.insertNewDay(Util.getToday(), steps);
            AddStep("1", steps, Util.getToday(), context);
        } else {
            db.addToLastEntry(db.getCurrentSteps());
        }
        // current steps will be reset on boot @see BootReceiver
        db.close();
    }

    private void AddStep(final String intentUserID, final int step, final long date, final Context context)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, StepUrls.ADD_STEP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                //Toast.makeText(HomeActivity.this,message,Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                            }

                            if (success.equals("0")) {
                                //Toast.makeText(HomeActivity.this,message,Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                            }

                        } catch (JSONException e) {
                            Toast.makeText(context,"Insert user step Error!" + e.toString(),Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("tagerror", "["+error+"]");
                        //Toast.makeText(HomeActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userID", intentUserID);
                params.put("step", String.valueOf(step));
                params.put("date", String.valueOf(date));
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

}
