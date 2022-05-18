package com.example.habitpadapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.habitpadapplication.Model.MyVoucher;
import com.example.habitpadapplication.R;

import java.util.ArrayList;
import java.util.List;

public class MyVoucherAdapter extends RecyclerView.Adapter<MyVoucherAdapter.MyViewHolder>{

    private Context mContext;
    private List<MyVoucher> myVouchers = new ArrayList<>();
//    private String intentUserID, voucherPhoto, voucherPoint, voucherTitle, voucherDesc;
//
//
//    private TextView voucherTitleTV, voucherPointTV, voucherDescTV;
//    private ImageView voucherPhotoView;


    public MyVoucherAdapter(Context context, List<MyVoucher> myVouchers){
        this.mContext = context;
        this.myVouchers = myVouchers;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView myVoucherPhoto;
        private TextView myRewardTitle;
        //private ConstraintLayout voucherLayout;

        public MyViewHolder (View view){
            super(view);

            //voucherLayout = view.findViewById(R.id.voucher_layout);
            myVoucherPhoto = view.findViewById(R.id.my_reward_image);
            myRewardTitle = view.findViewById(R.id.my_reward);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Intent intent = ((Activity) mContext).getIntent();
//        intentUserID = intent.getExtras().getString("intentUserID");

        View view = LayoutInflater.from(mContext).inflate(R.layout.my_reward_raw,parent,false);
        return new MyVoucherAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final MyVoucher myVoucher = myVouchers.get(position);


        Glide.with(mContext).asBitmap().load(myVoucher.getMyimage())
                .fitCenter()
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .fallback(R.drawable.ic_baseline_image_not_supported_24)
                .dontAnimate().into(holder.myVoucherPhoto);

        holder.myRewardTitle.setText(myVoucher.getMyreward());

    }

    @Override
    public int getItemCount() {
        return myVouchers.size();
    }


}
