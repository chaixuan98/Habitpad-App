package com.example.habitpadapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.habitpadapplication.AddFoodActivity;
import com.example.habitpadapplication.Model.DoctorAppointment;
import com.example.habitpadapplication.R;
import com.example.habitpadapplication.ViewUserProfile;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorAppointmentAdapter extends RecyclerView.Adapter<DoctorAppointmentAdapter.MyViewHolder>{

    private Context mContext;
    private List<DoctorAppointment> doctorAppointments = new ArrayList<>();

    public DoctorAppointmentAdapter (Context context, List<DoctorAppointment> doctorAppointments){
        this.mContext = context;
        this.doctorAppointments = doctorAppointments;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView uImg;
        private TextView uName, uDate, uTime, uRemark;
        private MaterialButton uProfileBtn;


        public MyViewHolder (View view){
            super(view);

            uImg = view.findViewById(R.id.app_user_image);
            uName = view.findViewById(R.id.app_user_name);
            uDate = view.findViewById(R.id.app_user_date);
            uTime = view.findViewById(R.id.app_user_time);
            uRemark = view.findViewById(R.id.app_user_remark);
            uProfileBtn = view.findViewById(R.id.user_profile_btn);

        }
    }

    @NonNull
    @Override
    public DoctorAppointmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.doctor_appointment_raw,parent,false);
        return new DoctorAppointmentAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAppointmentAdapter.MyViewHolder holder, int position) {
        final DoctorAppointment doctorAppointment = doctorAppointments.get(position);
        
        String userID = doctorAppointment.getUserID();

        holder.uName.setText(doctorAppointment.getUserName());
        holder.uDate.setText(doctorAppointment.getAppDate());
        holder.uTime.setText(doctorAppointment.getAppTime());
        holder.uRemark.setText(doctorAppointment.getAppRemark());


        Glide.with(holder.uImg.getContext())
                .asBitmap().load(doctorAppointment.getUserImg())
                .error(R.drawable.boy1)
                .fitCenter()
                .into(holder.uImg);

        holder.uProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendToUserProfilePage(userID);
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorAppointments.size();
    }

    private void SendToUserProfilePage(String userID)
    {
        Intent viewUserIntent = new Intent(mContext, ViewUserProfile.class);
        viewUserIntent.putExtra("userID", userID);
        mContext.startActivity(viewUserIntent);

    }

}
