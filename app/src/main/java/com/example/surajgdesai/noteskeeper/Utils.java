package com.example.surajgdesai.noteskeeper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Suraj G Desai on 2/27/2017.
 */

public class Utils {
    public static String getDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }

    public static Date getDateTime(String date) {
        try {
            return (Date) (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean getStatus(String value) {
        boolean returnFlag = false;
        if (value.equals("Pending"))
            returnFlag = false;
        else if (value.equals("Completed"))
            returnFlag = true;
        return returnFlag;
    }

    public static String getStatus(boolean value) {
        if (value)
            return "Completed";
        else
            return "Pending";
    }
}
