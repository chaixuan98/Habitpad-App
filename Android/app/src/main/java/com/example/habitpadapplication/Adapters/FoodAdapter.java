package com.example.habitpadapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.habitpadapplication.AddFoodActivity;
import com.example.habitpadapplication.Model.Food;
import com.example.habitpadapplication.R;

import java.util.ArrayList;
import java.util.List;


public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MyViewHolder>{

    private Context mContext;
    private List<Food> foods = new ArrayList<>();
    private String intentFrom,intentUserGender,intentUserAge,intentUserLifestyle,intentUserWeight,intentUserHeight;



    public FoodAdapter (Context context, List<Food> foods){
        this.mContext = context;
        this.foods = foods;

    }


    // method for filtering our recyclerview items.
    public void filterList(List<Food> filterllist) {
        // below line is to add our filtered list in our course array list.
        foods = filterllist;
        // below line is to notify our adaptermas change in recycler view data.
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView foodName, foodCalories;
        private LinearLayout foodLayout;


        public MyViewHolder (View view){
            super(view);

            foodName = view.findViewById(R.id.food_name);
            foodCalories = view.findViewById(R.id.food_calories);
            foodLayout= view.findViewById(R.id.food_layout);


        }
    }

    @NonNull
    @Override
    public FoodAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Intent intent = ((Activity) mContext).getIntent();
        intentFrom = intent.getExtras().getString("intentFrom");
        intentUserGender = intent.getExtras().getString("intentUserGender");
        intentUserAge = intent.getExtras().getString("intentUserAge");
        intentUserLifestyle = intent.getExtras().getString("intentUserLifestyle");
        intentUserWeight = intent.getExtras().getString("intentUserWeight");
        intentUserHeight = intent.getExtras().getString("intentUserHeight");


        View view = LayoutInflater.from(mContext).inflate(R.layout.food_raw,parent,false);
        return new FoodAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.MyViewHolder holder, int position) {

        final Food food = foods.get(position);

        final String foodID = food.getFoodID();
        String tempFoodName = food.getFoodname();

        if(!(TextUtils.isEmpty(tempFoodName)))
        {
            holder.foodName.setText(tempFoodName);
        }

        String tempCalories = food.getFoodcalories();
        if(!(TextUtils.isEmpty(tempCalories)))
        {
            holder.foodCalories.setText(tempCalories+"Cal");
        }

        holder.foodLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendUserToAddFoodPage(foodID);
            }
        });

    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    private void SendUserToAddFoodPage(String foodID)
    {
        Intent addFoodIntent = new Intent(mContext, AddFoodActivity.class);
        addFoodIntent.putExtra("intentFrom", intentFrom);
        addFoodIntent.putExtra("foodID", foodID);
        addFoodIntent.putExtra("intentUserGender", intentUserGender);
        addFoodIntent.putExtra("intentUserAge", intentUserAge);
        addFoodIntent.putExtra("intentUserLifestyle", intentUserLifestyle);
        addFoodIntent.putExtra("intentUserWeight", intentUserWeight);
        addFoodIntent.putExtra("intentUserHeight", intentUserHeight);
        mContext.startActivity(addFoodIntent);

    }
}
