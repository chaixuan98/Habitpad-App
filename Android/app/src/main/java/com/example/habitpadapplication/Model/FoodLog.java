package com.example.habitpadapplication.Model;

public class FoodLog {

    private String addFoodDetailID,foodName,addFoodDate,addFoodTime, totalFat,totalProtein, totalCarbs;
    private int numberOfServing,totalCalories;

    public FoodLog(String addFoodDetailID, String foodName, int numberOfServing , int totalCalories, String totalFat, String totalProtein, String totalCarbs, String addFoodDate , String addFoodTime ){
        this.addFoodDetailID = addFoodDetailID;
        this.foodName = foodName;
        this.numberOfServing = numberOfServing;
        this.totalCalories = totalCalories;
        this.totalFat = totalFat;
        this.totalProtein = totalProtein;
        this.totalCarbs = totalCarbs;
        this.addFoodDate = addFoodDate;
        this.addFoodTime = addFoodTime;

    }

    public String getAddFoodDetailID() {
        return addFoodDetailID;
    }
    public String getFoodName() {
        return foodName;
    }
    public int getNumberOfServing() {return numberOfServing;}
    public int getTotalCalories() {
        return totalCalories;
    }
    public String getTotalFat() {
        return totalFat;
    }
    public String getTotalProtein() {
        return totalProtein;
    }
    public String getTotalCarbs() {
        return totalCarbs;
    }
    public String getAddFoodDate() {
        return addFoodDate;
    }
    public String getAddFoodTime() {return addFoodTime;}
}
