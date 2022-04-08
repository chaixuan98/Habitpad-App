package com.example.habitpadapplication.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.habitpadapplication.Model.Tip;
import com.example.habitpadapplication.R;

import java.util.ArrayList;
import java.util.List;


public class TipViewAdpater extends RecyclerView.Adapter<TipViewAdpater.MyViewHolder> {

    private Context mContext;
    private List<Tip> tips = new ArrayList<>();


    public TipViewAdpater(Context context, List<Tip> tips){
        this.mContext = context;
        this.tips = tips;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tipDetails, tipDateTime;
        private ImageView tipPhoto;

        public MyViewHolder (View view){
            super(view);

            tipDetails = view.findViewById(R.id.desc_tv);
            tipPhoto = view.findViewById(R.id.tipimageview);
            tipDateTime = view.findViewById(R.id.dt_view);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.tip_display,parent,false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final Tip tip = tips.get(position);

        holder.tipDetails.setText(tip.getTipDetails());
        Glide.with(mContext).asBitmap().load(tip.getTipPhoto())
                .fitCenter()
                .placeholder(R.drawable.ic_baseline_image_24) //5
                .error(R.drawable.ic_baseline_broken_image_24) //6
                .fallback(R.drawable.ic_baseline_image_not_supported_24) //7
                .dontAnimate().into(holder.tipPhoto);
        holder.tipDateTime.setText(tip.getTipDateTime());

    }

    @Override
    public int getItemCount() {
        return tips.size();
    }



}