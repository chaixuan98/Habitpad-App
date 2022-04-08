package com.example.habitpadapplication.Model;

public class Food {

    public String foodID,foodname, foodservingsizeunit, foodcalories, foodfat, foodprotein, foodcarbs;


    public Food(String foodID, String foodname, String foodservingsizeunit, String foodcalories, String foodfat, String foodprotein, String foodcarbs)
    {
        this.foodID = foodID;
        this.foodname = foodname;
        this.foodservingsizeunit = foodservingsizeunit;
        this.foodcalories = foodcalories;
        this.foodfat = foodfat;
        this.foodprotein = foodprotein;
        this.foodcarbs = foodcarbs;
    }

    public String getFoodID() {
        return foodID;
    }

    public String getFoodname() {
        return foodname;
    }

    public String getFoodservingsizeunit() {
        return foodservingsizeunit;
    }

    public String getFoodcalories() {
        return foodcalories;
    }

    public String getFoodfat() {
        return foodfat;
    }

    public String getFoodprotein() {
        return foodprotein;
    }

    public String getFoodcarbs() {
        return foodcarbs;
    }




}
