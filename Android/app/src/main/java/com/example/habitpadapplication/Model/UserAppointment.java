package com.example.habitpadapplication.Model;

public class UserAppointment {

    String appID, drID, drImg, drName, appDate, appTime, appRemark;

    UserAppointment() {

    }
    public UserAppointment(String appID, String drID, String drImg, String drName, String appDate, String appTime, String appRemark) {
        this.appID = appID;
        this.drID = drID;
        this.drImg = drImg;
        this.drName = drName;
        this.appDate = appDate;
        this.appTime = appTime;
        this.appRemark = appRemark;

    }

    public String getAppID() {
        return appID;
    }

    public String getDrID() {
        return drID;
    }

    public String getDrImg() {
        return drImg;
    }

    public String getDrName() {
        return drName;
    }

    public String getAppDate() {
        return appDate;
    }

    public String getAppTime() {
        return appTime;
    }

    public String getAppRemark() {
        return appRemark;
    }



}
