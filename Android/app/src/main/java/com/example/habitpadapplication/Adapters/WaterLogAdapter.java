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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.habitpadapplication.HomeActivity;
import com.example.habitpadapplication.Model.WaterLog;
import com.example.habitpadapplication.R;
import com.example.habitpadapplication.Urls;
import com.example.habitpadapplication.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaterLogAdapter extends RecyclerView.Adapter<WaterLogAdapter.MyViewHolder>{

    private Context mContext;
    private List<WaterLog> waterLogs = new ArrayList<>();


    // Volley variables
    private StringRequest mStringRequest;
    private RequestQueue mRequestQueue;


    public WaterLogAdapter(Context context, List<WaterLog> waterLogs){
        this.mContext = context;
        this.waterLogs = waterLogs;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView waterLog;
        private ImageView typContainer;
        private LinearLayout waterLogLayout;

        public MyViewHolder (View view){
            super(view);

            waterLog = view.findViewById(R.id.amount);
            typContainer = view.findViewById(R.id.containerTyp);
            waterLogLayout = view.findViewById(R.id.water_log_layout);
        }
    }

    @NonNull
    @Override
    public WaterLogAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.time_log_raw,parent,false);
        return new WaterLogAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WaterLogAdapter.MyViewHolder holder, @SuppressLint("RecyclerView")  int position) {

        final WaterLog waterLog = waterLogs.get(position);

        holder.waterLog.setText(waterLog.getAmount()+ " ml");

        if(waterLog.getContainerTyp().equals("glass"))
            Glide.with(mContext).load(R.drawable.ic_glass).into(holder.typContainer);
        if(waterLog.getContainerTyp().equals("coffee"))
            Glide.with(mContext).load(R.drawable.ic_cofee).into(holder.typContainer);
        if(waterLog.getContainerTyp().equals("tea"))
            Glide.with(mContext).load(R.drawable.ic_tea).into(holder.typContainer);
        if(waterLog.getContainerTyp().equals("cola"))
            Glide.with(mContext).load(R.drawable.ic_cola).into(holder.typContainer);
        if (waterLog.getContainerTyp().equals("juice"))
            Glide.with(mContext).load(R.drawable.ic_juice).into(holder.typContainer);
        if (waterLog.getContainerTyp().equals("other"))
            Glide.with(mContext).load(R.drawable.ic_milk).into(holder.typContainer);

        holder.waterLogLayout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Delete Water Log");
            builder.setMessage("Conform to Delete this water log");
            builder.setNegativeButton("CANCEL", (dialog, i) -> dialog.dismiss());
            builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.DELETE_WATER_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.i("tagconvertstr", "["+response+"]");
                                        JSONObject jsonObject = new JSONObject(response);
                                        String check = jsonObject.getString("success");
                                        if (check.equals("1")){
                                            Delete(position);
                                            Toast.makeText(mContext, "Delete Water Log Sucessfully",Toast.LENGTH_LONG).show();
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
                            deleteParams.put("waterTimeID", waterLog.getWaterTimeID());

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
        return waterLogs.size();
    }

    public void Delete(int item){
        waterLogs.remove(item);
        notifyItemRemoved(item);

    }
}
