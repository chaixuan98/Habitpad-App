package com.example.habitpadapplication.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.habitpadapplication.Model.Doctor;
import com.example.habitpadapplication.Model.UserAppointment;
import com.example.habitpadapplication.R;
import com.example.habitpadapplication.Urls;
import com.example.habitpadapplication.VolleySingleton;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAppointmetAdapter extends RecyclerView.Adapter<UserAppointmetAdapter.MyViewHolder>{

    private Context mContext;
    private List<UserAppointment> userAppointments = new ArrayList<>();
    private String intentUserID;


    public UserAppointmetAdapter (Context context, List<UserAppointment> userAppointments){
        this.mContext = context;
        this.userAppointments = userAppointments;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView dImg;
        private TextView dName, dDate, dTime, dRemark;
        private MaterialButton deleteBtn;


        public MyViewHolder (View view){
            super(view);

            dImg = view.findViewById(R.id.app_dr_image);
            dName = view.findViewById(R.id.app_dr_name);
            dDate = view.findViewById(R.id.app_dr_date);
            dTime = view.findViewById(R.id.app_dr_time);
            dRemark = view.findViewById(R.id.app_dr_remark);
            deleteBtn = view.findViewById(R.id.app_delete_btn);

        }
    }

    @NonNull
    @Override
    public UserAppointmetAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_appointment_raw,parent,false);
        return new UserAppointmetAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAppointmetAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final UserAppointment userAppointment = userAppointments.get(position);

        String docName = userAppointment.getDrName();

        holder.dName.setText("Dr."+docName);
        holder.dDate.setText(userAppointment.getAppDate());
        holder.dTime.setText(userAppointment.getAppTime());
        holder.dRemark.setText(userAppointment.getAppRemark());


        Glide.with(holder.dImg.getContext())
                .asBitmap().load(userAppointment.getDrImg())
                .error(R.drawable.doctor_avatar_white)
                .fitCenter()
                .into(holder.dImg);

        holder.deleteBtn.setOnClickListener((View v) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Delete Appointment");
            builder.setMessage("Are you sure you want to Delete this Appointment ?");
            builder.setNegativeButton("No", (dialog, i) -> dialog.dismiss());
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.DELETE_USER_APPOINTMENT_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.i("tagconvertstr", "["+response+"]");
                                        JSONObject jsonObject = new JSONObject(response);
                                        String check = jsonObject.getString("success");
                                        if (check.equals("1")){
                                            Delete(position);
                                            UpdateSlot(userAppointment.getDrID(),userAppointment.getAppDate() ,userAppointment.getAppTime(),"Available");
                                            Toast.makeText(mContext, "Delete Appointment Sucessfully",Toast.LENGTH_LONG).show();

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
                            deleteParams.put("appointmentID", userAppointment.getAppID());

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
        return userAppointments.size();
    }

    public void Delete(int item){
        userAppointments.remove(item);
        notifyItemRemoved(item);

    }

    private void UpdateSlot(final String dr_ID,final String date, final String time,final String slotAvailable) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.UPDATE_DOCTOR_SLOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            Toast.makeText(mContext,"Save Error!" + e.toString(),Toast.LENGTH_LONG).show();
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
                params.put("doctorID", dr_ID );
                params.put("slotDate", date);
                params.put("slotTime", time);
                params.put("slotAvailable", slotAvailable);



                return params;
            }
        };

        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);

    }


}
