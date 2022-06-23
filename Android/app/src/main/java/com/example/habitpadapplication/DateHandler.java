package com.example.habitpadapplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHandler {
//    public static String getCurrentDate(){
//        return  new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss").format(new Date());
//    }

    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "HH:mm a", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    public static String getDateFormat(String date)  {
        System.out.println(date);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fmt = new SimpleDateFormat("yyyy-MM-dd");
        return fmt.format(d);
    }

    public static String getCurrentFormedDate(){
        return  new SimpleDateFormat( "yyyy-MM-dd").format(new Date());
    }

    public static String dateFormat1(String date) {
        System.out.println(" time string: "+date);
        SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
        Date d = null;
        try {
            d = fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fmt = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(" time: "+fmt.format(d));
        return fmt.format(d);

    }

    public static String dateFormat2(String date) {
        System.out.println(" time string: "+date);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fmt = new SimpleDateFormat("dd/MM");
        System.out.println(" time: "+fmt.format(d));
        return fmt.format(d);

    }

    public static String dateFormat3(String date) {
        System.out.println(" time string: "+date);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fmt = new SimpleDateFormat("yyyyMMdd");
        System.out.println(" time: "+fmt.format(d));
        return fmt.format(d);

    }


    public static String timeFormat(String date) {
        System.out.println(" time string: "+date);
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
        Date d = null;
        try {
            d = fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fmt = new SimpleDateFormat("HH:mm a");
        System.out.println(" time: "+fmt.format(d));
        return fmt.format(d);

    }

    public static String dayFormat(String date) {
        System.out.println(" date string: "+date);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = null;
        try {
            d = fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fmt = new SimpleDateFormat("EEE");
        System.out.println(" date: "+fmt.format(d));
        return fmt.format(d);

    }

    public static String monthFormat(String date) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fmt = new SimpleDateFormat("MMM");
        return fmt.format(d);

    }
    public static String monthFormat2(String date) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fmt = new SimpleDateFormat("M");
        return fmt.format(d);

    }

    public static String monthFormat3(String date) {
        SimpleDateFormat fmt = new SimpleDateFormat("MMM");
        Date d = null;
        try {
            d = fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fmt = new SimpleDateFormat("M");
        return fmt.format(d);

    }
    public static String yearFormat(String date) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fmt = new SimpleDateFormat("yyyy");
        return fmt.format(d);

    }
    public static String dAndmFormat(String date) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fmt = new SimpleDateFormat("d MMM");
        return fmt.format(d);

    }
    public static String dateFormat(String date) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fmt = new SimpleDateFormat("EEE, MMM d, ''yy");
        return fmt.format(d);

    }
}
