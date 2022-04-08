package com.example.habitpadapplication.Adapters;

import android.annotation.SuppressLint;
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
import com.example.habitpadapplication.HomeActivity;
import com.example.habitpadapplication.Model.FoodLog;
import com.example.habitpadapplication.R;
import com.example.habitpadapplication.Urls;
import com.example.habitpadapplication.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodLogAdapter extends RecyclerView.Adapter<FoodLogAdapter.MyViewHolder>  {

    private Context mContext;
    private List<FoodLog> foodLogs = new ArrayList<>();


    public FoodLogAdapter(Context context, List<FoodLog> foodLogs){
        this.mContext = context;
        this.foodLogs = foodLogs;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView totalFat,totalProtein,totalCarbs, servingNumber,foodName, totalCalories;
        private LinearLayout foodLogLayout;


        public MyViewHolder (View view){
            super(view);

            foodLogLayout = view.findViewById(R.id.food_log_layout);
            totalFat = view.findViewById(R.id.userfood_totalFat);
            totalProtein = view.findViewById(R.id.userfood_totalProtein);
            totalCarbs = view.findViewById(R.id.userfood_totalCarbs);
            servingNumber = view.findViewById(R.id.userfood_servingnumber);
            foodName = view.findViewById(R.id.userfood_name);
            totalCalories = view.findViewById(R.id.userfood_totalCalories);

        }
    }



    @NonNull
    @Override
    public FoodLogAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_food_layout,parent,false);
        return new FoodLogAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodLogAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final FoodLog foodLog = foodLogs.get(position);

        final String totalFatValue = foodLog.getTotalFat();
        final String totalProteinValue = foodLog.getTotalProtein();
        final String totalCarbsValue = foodLog.getTotalCarbs();

        final int servingNumberValue = foodLog.getNumberOfServing();
        final String foodNameValue = foodLog.getFoodName();
        final int totalCaloriesValue= foodLog.getTotalCalories();

        holder.totalFat.setText("F: "+ totalFatValue+ " g");
        holder.totalProtein.setText("P: "+ totalProteinValue+ " g");
        holder.totalCarbs.setText("C: "+ totalCarbsValue+ " g");

        holder.servingNumber.setText(servingNumberValue+ "x");
        holder.foodName.setText(foodNameValue);
        holder.totalCalories.setText(totalCaloriesValue+ " Cal");

        holder.foodLogLayout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Delete Food Log");
            builder.setMessage("Conform to Delete this food log");
            builder.setNegativeButton("CANCEL", (dialog, i) -> dialog.dismiss());
            builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.DELETE_FOOD_LOG_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.i("tagconvertstr", "["+response+"]");
                                        JSONObject jsonObject = new JSONObject(response);
                                        String check = jsonObject.getString("success");
                                        if (check.equals("1")){
                                            Delete(position);
                                            Toast.makeText(mContext, "Delete Food Log Sucessfully",Toast.LENGTH_LONG).show();
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
                            deleteParams.put("addFoodDetailID", foodLog.getAddFoodDetailID());

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
        return foodLogs.size();
    }

    public void Delete(int item) {
        foodLogs.remove(item);
        notifyItemRemoved(item);
    }
}
