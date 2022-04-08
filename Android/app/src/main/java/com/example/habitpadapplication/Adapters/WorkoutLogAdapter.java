package com.example.habitpadapplication.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.habitpadapplication.Chart.WorkoutChart;
import com.example.habitpadapplication.DiaryActivity;
import com.example.habitpadapplication.HomeActivity;
import com.example.habitpadapplication.Model.WorkoutLog;
import com.example.habitpadapplication.R;
import com.example.habitpadapplication.Urls;
import com.example.habitpadapplication.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkoutLogAdapter extends RecyclerView.Adapter<WorkoutLogAdapter.MyViewHolder>{

    private Context mContext;
    private List<WorkoutLog> workoutLogs = new ArrayList<>();

    private String intentUserID,intentUserWeight;


    public WorkoutLogAdapter(Context context, List<WorkoutLog> workoutLogs){
        this.mContext = context;
        this.workoutLogs = workoutLogs;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView duration,workoutName, totalCalories;
        private LinearLayout workoutLogLayout;


        public MyViewHolder (View view){
            super(view);

            duration = view.findViewById(R.id.userworkout_duration);
            workoutName = view.findViewById(R.id.userworkout_name);
            totalCalories = view.findViewById(R.id.userworkout_totalCalories);
            workoutLogLayout = view.findViewById(R.id.workout_log_layout);

        }
    }


    @NonNull
    @Override
    public WorkoutLogAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.user_workout_layout,parent,false);
        return new WorkoutLogAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutLogAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final WorkoutLog workoutLog = workoutLogs.get(position);

        final String durationValue = workoutLog.getNumberOfDuration();
        final String workoutNameValue = workoutLog.getWorkoutName();
        final String totalCaloriesValue = workoutLog.getTotalCalories();

        holder.duration.setText(durationValue+ " min");
        holder.workoutName.setText(workoutNameValue);
        holder.totalCalories.setText(totalCaloriesValue+ " Cal");
        holder.workoutLogLayout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Delete Workout Log");
            builder.setMessage("Conform to Delete this workout log");
            builder.setNegativeButton("CANCEL", (dialog, i) -> dialog.dismiss());
            builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.DELETE_WORKOUT_LOG_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.i("tagconvertstr", "["+response+"]");
                                        JSONObject jsonObject = new JSONObject(response);
                                        String check = jsonObject.getString("success");
                                        if (check.equals("1")){
                                            Delete(position);
                                            Toast.makeText(mContext, "Delete Workout Sucessfully",Toast.LENGTH_LONG).show();
                                            Intent getHomeIntent = new Intent(mContext, HomeActivity.class);
                                            mContext.startActivity(getHomeIntent);

                                        }else{
                                            Toast.makeText(mContext, response,Toast.LENGTH_LONG).show();
                                        }
                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(mContext, error.toString(),Toast.LENGTH_LONG).show();
                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError
                        {
                            Map<String, String> deleteParams = new HashMap<>();
                            deleteParams.put("addWorkoutDetailID", workoutLog.getAddWorkoutDetailID());

                            return deleteParams;
                        }
                    };
                    VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        });


    }

    @Override
    public int getItemCount() {
        return workoutLogs.size();
    }

    public void Delete(int item){
        workoutLogs.remove(item);
        notifyItemRemoved(item);

    }
}
