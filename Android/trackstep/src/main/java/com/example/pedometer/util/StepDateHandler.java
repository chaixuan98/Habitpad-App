package com.example.pedometer.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StepDateHandler {

    public static String getCurrentFormedDate(){
        return  new SimpleDateFormat( "yyyy-MM-dd").format(new Date());
    }
}
