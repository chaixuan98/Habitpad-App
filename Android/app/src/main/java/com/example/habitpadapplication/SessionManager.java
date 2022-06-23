package com.example.habitpadapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    //variables
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN = "IsLoggedIn";

    public  static  final String KEY_USERID = "userID";
    public  static  final String KEY_USERPHOTO = "userPhoto";
    public  static  final String KEY_USERNAME = "username";
    public  static  final String KEY_EMAIL = "email";
    public  static  final String KEY_PHONE = "phone";
    public  static  final String KEY_PASSWORD = "password";
    public  static  final String KEY_GENDER = "gender";
    public  static  final String KEY_AGE = "age";
    public  static  final String KEY_WEIGHT = "weight";
    public  static  final String KEY_HEIGHT = "height";
    public  static  final String KEY_FAMILY_SUFFERED = "familySuffered";
    public  static  final String KEY_LIFESTYLE = "lifestyle";
    public  static  final String KEY_BMI = "bmi";
    public  static  final String KEY_SMOKED = "smoked";
    public  static  final String KEY_ALCOHOL = "familySuffered";
    public  static  final String KEY_MEDICAL = "medical";


    public SessionManager(Context _context){
        context = _context;
        sharedPreferences = _context.getSharedPreferences("userLoginSession",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void  createLoginSession(String userID,String userPhoto, String username, String email, String phone, String password, String gender, String age, String weight, String height,String familySuffered,String lifestyle, String bmi ,String smoked,String alcohol, String medical){

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERID ,userID);
        editor.putString(KEY_USERPHOTO ,userPhoto);
        editor.putString(KEY_USERNAME ,username);
        editor.putString(KEY_EMAIL ,email);
        editor.putString(KEY_PHONE ,phone);
        editor.putString(KEY_PASSWORD ,password);
        editor.putString(KEY_GENDER ,gender);
        editor.putString(KEY_AGE ,age);
        editor.putString(KEY_WEIGHT ,weight);
        editor.putString(KEY_HEIGHT ,height);
        editor.putString(KEY_FAMILY_SUFFERED ,familySuffered);
        editor.putString(KEY_LIFESTYLE ,lifestyle);
        editor.putString(KEY_BMI ,bmi);
        editor.putString(KEY_SMOKED ,smoked);
        editor.putString(KEY_ALCOHOL ,alcohol);
        editor.putString(KEY_MEDICAL ,medical);
        editor.commit();
    }

    public HashMap<String,String>getUsersDetailFromSession(){
        HashMap<String,String> userData = new HashMap<String,String>();
        userData.put(KEY_USERID, sharedPreferences.getString(KEY_USERID,null));
        userData.put(KEY_USERPHOTO, sharedPreferences.getString(KEY_USERPHOTO,null));
        userData.put(KEY_USERNAME, sharedPreferences.getString(KEY_USERNAME,null));
        userData.put(KEY_EMAIL, sharedPreferences.getString(KEY_EMAIL,null));
        userData.put(KEY_PHONE, sharedPreferences.getString(KEY_PHONE,null));
        userData.put(KEY_PASSWORD, sharedPreferences.getString(KEY_PASSWORD,null));
        userData.put(KEY_GENDER, sharedPreferences.getString(KEY_GENDER,null));
        userData.put(KEY_AGE, sharedPreferences.getString(KEY_AGE,null));
        userData.put(KEY_WEIGHT, sharedPreferences.getString(KEY_WEIGHT,null));
        userData.put(KEY_HEIGHT, sharedPreferences.getString(KEY_HEIGHT,null));
        userData.put(KEY_FAMILY_SUFFERED, sharedPreferences.getString(KEY_FAMILY_SUFFERED,null));
        userData.put(KEY_LIFESTYLE, sharedPreferences.getString(KEY_LIFESTYLE,null));
        userData.put(KEY_BMI, sharedPreferences.getString(KEY_BMI,null));
        userData.put(KEY_SMOKED, sharedPreferences.getString(KEY_SMOKED,null));
        userData.put(KEY_ALCOHOL, sharedPreferences.getString(KEY_ALCOHOL,null));
        userData.put(KEY_MEDICAL, sharedPreferences.getString(KEY_MEDICAL,null));

        return  userData;
    }

    public  boolean isUserLogin(){
        return sharedPreferences.getBoolean(IS_LOGIN,false);

    }

    public void checkLogin(){
        if (!this.isUserLogin()){
            Intent i = new Intent(context, LoginActivity.class);
            //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            //((HomeActivity) context).finish();
        }

    }


    public void logoutUserFromSession(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
        //((HomeActivity) context).finish();

    }

//    public boolean getFirstTimeRunPrefs(){
//        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        return sharedPreferences.getBoolean("first_time_run", true) ;
//    }
//
//    public void setFirstTimeRunPrefs(boolean b) {
//       // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//       // SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean("first_time_run", b);
//        editor.commit();
//    }

}
