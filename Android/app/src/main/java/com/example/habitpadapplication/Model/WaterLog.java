package com.example.habitpadapplication.Model;

public class WaterLog {


    private String waterTimeID,userID,containerTyp,waterDateTime,waterTime;
    private int drunkWaterOnce;

    public WaterLog(String waterTimeID, String userID, int drunkWaterOnce , String containerTyp, String waterDateTime , String waterTime ){
        this.waterTimeID = waterTimeID;
        this.userID = userID;
        this.drunkWaterOnce = drunkWaterOnce;
        this.containerTyp = containerTyp;
        this.waterDateTime = waterDateTime;
        this.waterTime = waterTime;

    }


    public String getWaterTimeID() {
        return waterTimeID;
    }
    public String getUserID() {
        return userID;
    }
    public int getAmount() {return drunkWaterOnce;}
    public String getContainerTyp() {
        return containerTyp;
    }
    public String getDate() {
        return waterDateTime;
    }
    public String getTime() {return waterTime;}







}
