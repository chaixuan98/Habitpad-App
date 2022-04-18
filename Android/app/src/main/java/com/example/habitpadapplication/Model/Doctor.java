package com.example.habitpadapplication.Model;

public class Doctor {


    String drID, img, dr_name, dr_email, dr_phone, degree, exp, place;

    Doctor() {

    }
    public Doctor(String drID, String img, String dr_name, String dr_email, String dr_phone, String degree, String exp,  String place) {
        this.drID = drID;
        this.img = img;
        this.dr_name = dr_name;
        this.dr_email = dr_email;
        this.dr_phone = dr_phone;
        this.degree = degree;
        this.exp = exp;
        this.place = place;

    }

    public String getDrID() {
        return drID;
    }

    public String getImg() {
        return img;
    }

    public String getDr_name() {
        return dr_name;
    }

    public String getDr_email() {
        return dr_email;
    }

    public String getDr_phone() {
        return dr_phone;
    }

    public String getDegree() {
        return degree;
    }

    public String getExp() {
        return exp;
    }

    public String getPlace() {
        return place;
    }



}