package com.example.habitpadapplication.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.bumptech.glide.Glide;
import com.example.habitpadapplication.Model.UserAppointment;
import com.example.habitpadapplication.R;
import com.example.habitpadapplication.Urls;
import com.example.habitpadapplication.VolleySingleton;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserPastAppointmentAdapter extends RecyclerView.Adapter<UserPastAppointmentAdapter.MyViewHolder>{
    private Context mContext;
    private List<UserAppointment> userAppointments = new ArrayList<>();
    private String intentUserID;


    public UserPastAppointmentAdapter (Context context, List<UserAppointment> userAppointments){
        this.mContext = context;
        this.userAppointments = userAppointments;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView dImg;
        private TextView dName, dDate, dTime, dRemark, uAdvice;
        private LinearLayout adviceLayout;


        public MyViewHolder (View view){
            super(view);

            dImg = view.findViewById(R.id.app_dr_image);
            dName = view.findViewById(R.id.app_dr_name);
            dDate = view.findViewById(R.id.app_dr_date);
            dTime = view.findViewById(R.id.app_dr_time);
            dRemark = view.findViewById(R.id.app_dr_remark);


            adviceLayout = view.findViewById(R.id.advice_layout);
            uAdvice = view.findViewById(R.id.advice_detail);

        }
    }
    @NonNull
    @Override
    public UserPastAppointmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_past_appointment_raw,parent,false);
        return new UserPastAppointmentAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserPastAppointmentAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final UserAppointment userAppointment = userAppointments.get(position);

        String docName = userAppointment.getDrName();
        String appID = userAppointment.getAppID();

        holder.dName.setText("Dr."+docName);
        holder.dDate.setText(userAppointment.getAppDate());
        holder.dTime.setText(userAppointment.getAppTime());
        holder.dRemark.setText(userAppointment.getAppRemark());


        Glide.with(holder.dImg.getContext())
                .asBitmap().load(userAppointment.getDrImg())
                .error(R.drawable.doctor_avatar_white)
                .fitCenter()
                .into(holder.dImg);



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_ADVICE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("advice");

                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String adviceDetail = object.getString("adviceDetail").trim();

                                    holder.adviceLayout.setVisibility(View.VISIBLE);
                                    holder.uAdvice.setText(adviceDetail);


                                }
                            }

                            if (success.equals("0")) {
                                holder.adviceLayout.setVisibility(View.GONE);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("appointmentID", appID);
                return params;
            }
        };

        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);


    }

    @Override
    public int getItemCount() {
        return userAppointments.size();
    }

}
