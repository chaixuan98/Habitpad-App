package com.example.habitpadapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.habitpadapplication.BookAppointment;
import com.example.habitpadapplication.Model.Doctor;
import com.example.habitpadapplication.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.MyViewHolder>{

    private Context mContext;
    private List<Doctor> doctors = new ArrayList<>();



    public DoctorAdapter (Context context, List<Doctor> doctors){
        this.mContext = context;
        this.doctors = doctors;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView dImg;
        private TextView dName, dEmail, dPhone, dDegree, dExp, dPlace ;
        private MaterialButton dBtn;


        public MyViewHolder (View view){
            super(view);

            dImg = view.findViewById(R.id.dr_card_image);
            dName = view.findViewById(R.id.dr_card_name);
            dEmail = view.findViewById(R.id.dr_card_email);
            dPhone = view.findViewById(R.id.dr_card_phone);
            dDegree = view.findViewById(R.id.dr_card_degree);
            dExp = view.findViewById(R.id.dr_card_exp);
            dPlace = view.findViewById(R.id.dr_card_place);
            dBtn = view.findViewById(R.id.book_btn);


        }
    }


    @NonNull
    @Override
    public DoctorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.doctor_raw,parent,false);
        return new DoctorAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAdapter.MyViewHolder holder, int position) {

        final Doctor doctor = doctors.get(position);

        holder.dName.setText(doctor.getDr_name());
        holder.dEmail.setText(doctor.getDr_email());
        holder.dPhone.setText(doctor.getDr_phone());
        holder.dDegree.setText(doctor.getDegree());
        holder.dExp.setText(doctor.getExp());
        holder.dPlace.setText(doctor.getPlace());

        Glide.with(holder.dImg.getContext())
                .asBitmap().load(doctor.getImg())
                .error(R.drawable.doctor_avatar_white)
                .fitCenter()
                .into(holder.dImg);

        holder.dBtn.setOnClickListener(v -> {
            Intent intent = new Intent(mContext.getApplicationContext(), BookAppointment.class);
            intent.putExtra("dr_app_ID", doctor.getDrID());
            intent.putExtra("dr_app_img", doctor.getImg());
            intent.putExtra("dr_app_name", doctor.getDr_name());
            intent.putExtra("dr_app_email", doctor.getDr_email());
            intent.putExtra("dr_app_phone", doctor.getDr_phone());
            intent.putExtra("dr_app_exp", doctor.getExp());
            intent.putExtra("dr_app_degree", doctor.getDegree());
            intent.putExtra("dr_app_place", doctor.getPlace());



            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.getApplicationContext().startActivity(intent);
        });



    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }
}
