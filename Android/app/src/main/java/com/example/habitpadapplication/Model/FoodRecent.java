package com.example.habitpadapplication.Model;

public class FoodRecent {

    public String foodID,foodname, foodcalories;


    public FoodRecent(String foodID, String foodname, String foodcalories)
    {
        this.foodID = foodID;
        this.foodname = foodname;
        this.foodcalories = foodcalories;

    }

    public String getFoodID() {
        return foodID;
    }

    public String getFoodname() {
        return foodname;
    }

    public String getFoodcalories() {
        return foodcalories;
    }



}
