package com.example.habitpadapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class DoctorSessionManager {

    //variables
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN = "IsLoggedIn";

    public  static  final String KEY_DOCTORID = "doctorID";
    public  static  final String KEY_DOCTORNAME = "doctorName";
    public  static  final String KEY_DOCTOREMAIL = "doctorEmail";
    public  static  final String KEY_DOCTORPHONE = "doctorPhone";
    public  static  final String KEY_DOCTORPASSWORD = "doctorPassword";
    public  static  final String KEY_EDUCATION = "doctorEducation";
    public  static  final String KEY_EXP = "doctorExp";
    public  static  final String KEY_HOSPITAL = "doctorHospital";


    public DoctorSessionManager(Context _context){
        context = _context;
        sharedPreferences = _context.getSharedPreferences("doctorLoginSession",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void createDoctorLoginSession(String doctorID, String doctorName, String doctorPassword, String doctorEmail, String doctorPhone,  String doctorEducation, String doctorExp, String doctorHospital){

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_DOCTORID ,doctorID);
        editor.putString(KEY_DOCTORNAME ,doctorName);
        editor.putString(KEY_DOCTOREMAIL ,doctorEmail);
        editor.putString(KEY_DOCTORPHONE ,doctorPhone);
        editor.putString(KEY_DOCTORPASSWORD ,doctorPassword);
        editor.putString(KEY_EDUCATION ,doctorEducation);
        editor.putString(KEY_EXP ,doctorExp);
        editor.putString(KEY_HOSPITAL ,doctorHospital);

        editor.commit();
    }

    public HashMap<String,String> getDoctorDetailFromSession(){
        HashMap<String,String> doctorData = new HashMap<String,String>();
        doctorData.put(KEY_DOCTORID, sharedPreferences.getString(KEY_DOCTORID,null));
        doctorData.put(KEY_DOCTORNAME, sharedPreferences.getString(KEY_DOCTORNAME,null));
        doctorData.put(KEY_DOCTOREMAIL, sharedPreferences.getString(KEY_DOCTOREMAIL,null));
        doctorData.put(KEY_DOCTORPHONE, sharedPreferences.getString(KEY_DOCTORPHONE,null));
        doctorData.put(KEY_DOCTORPASSWORD, sharedPreferences.getString(KEY_DOCTORPASSWORD,null));
        doctorData.put(KEY_EDUCATION, sharedPreferences.getString(KEY_EDUCATION,null));
        doctorData.put(KEY_EXP, sharedPreferences.getString(KEY_EXP,null));
        doctorData.put(KEY_HOSPITAL, sharedPreferences.getString(KEY_HOSPITAL,null));

        return  doctorData;
    }

    public  boolean isUserLogin(){
        return sharedPreferences.getBoolean(IS_LOGIN,false);

    }

    public void checkLogin(){
        if (!this.isUserLogin()){
            Intent i = new Intent(context, UserOptionActivity.class);
            context.startActivity(i);
            //((HomeActivity) context).finish();
        }

    }


    public void logoutDoctorFromSession(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, UserOptionActivity.class);
        context.startActivity(i);
        //((HomeActivity) context).finish();

    }

    public boolean getFirstTimeRunPrefs(){
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean("first_time_run", true) ;
    }

    public void setFirstTimeRunPrefs(boolean b) {
        // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        // SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("first_time_run", b);
        editor.commit();
    }
}
