package com.example.habitpadapplication.Model;

public class DoctorAppointment {

    String appID, userID, userImg, userName, appDate, appTime, appRemark;

    DoctorAppointment() {

    }
    public DoctorAppointment(String appID, String userID, String userImg, String userName, String appDate, String appTime, String appRemark) {
        this.appID = appID;
        this.userID = userID;
        this.userImg = userImg;
        this.userName = userName;
        this.appDate = appDate;
        this.appTime = appTime;
        this.appRemark = appRemark;

    }

    public String getAppID() {
        return appID;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserImg() {
        return userImg;
    }

    public String getUserName() {
        return userName;
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
