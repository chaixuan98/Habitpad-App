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
import com.example.habitpadapplication.Model.MyGift;
import com.example.habitpadapplication.Model.MyVoucher;
import com.example.habitpadapplication.R;

import java.util.ArrayList;
import java.util.List;

public class MyGiftAdpater extends RecyclerView.Adapter<MyGiftAdpater.MyViewHolder>{

    private Context mContext;
    private List<MyGift> myGifts = new ArrayList<>();


    public MyGiftAdpater(Context context, List<MyGift> myGifts){
        this.mContext = context;
        this.myGifts = myGifts;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView myGiftPhoto;
        private TextView myGiftTitle;
        //private ConstraintLayout giftLayout;

        public MyViewHolder (View view){
            super(view);

            //giftLayout = view.findViewById(R.id.gift_layout);
            myGiftPhoto = view.findViewById(R.id.my_gift_image);
            myGiftTitle = view.findViewById(R.id.my_gift_reward);

        }
    }

    @NonNull
    @Override
    public MyGiftAdpater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Intent intent = ((Activity) mContext).getIntent();
//        intentUserID = intent.getExtras().getString("intentUserID");

        View view = LayoutInflater.from(mContext).inflate(R.layout.my_gift_raw,parent,false);
        return new MyGiftAdpater.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyGiftAdpater.MyViewHolder holder, int position) {

        final MyGift myGift = myGifts.get(position);


        Glide.with(mContext).asBitmap().load(myGift.getMyGiftImage())
                .fitCenter()
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .fallback(R.drawable.ic_baseline_image_not_supported_24)
                .dontAnimate().into(holder.myGiftPhoto);

        holder.myGiftTitle.setText(myGift.getMyGiftReward());

    }

    @Override
    public int getItemCount() {
        return myGifts.size();
    }
}
