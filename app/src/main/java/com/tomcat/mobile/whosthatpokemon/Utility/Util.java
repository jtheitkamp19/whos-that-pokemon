package com.tomcat.mobile.whosthatpokemon.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.tomcat.mobile.whosthatpokemon.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Util {
    private static Util util = null;
    private static Context appContext;
    private static SharedPreferences pref;
    private final String APP_PROPERTY_HAS_LOCAL_DATA = "propertyHasLocalData";

    public static final int GAME_TIME = 300;
    public static final int ONE_SECOND_IN_MILLISECONDS = 1000;

    private Util() {

    }

    public static void setInstance(Context context) {
        appContext = context;
        pref = appContext.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        util = new Util();
    }

    public static synchronized Util getInstance() {
        return util;
    }

    private SharedPreferences.Editor getPropertyEditor() {
        return pref.edit();
    }

    public void setPropertyHasLocalData(boolean hasLocalData) {
        getPropertyEditor().putBoolean(APP_PROPERTY_HAS_LOCAL_DATA, hasLocalData).commit();
    }

    public boolean getPropertyHasLocalData() {
        return pref.getBoolean(APP_PROPERTY_HAS_LOCAL_DATA, false);
    }

    public String getWhereClauseWhereKeyIsValue(String key, String value) {
        return key + " = " + wrap(value);
    }

    public String getWhereClauseWhereKeyIsValue(String key, int value) {
        return key + " = " + wrap(value);
    }

    public String capitalizeFirstLetter(String str) {
        String body = str;
        try {
            body = str.substring(1);
            body = str.toUpperCase().charAt(0) + body;
        } catch (IndexOutOfBoundsException ioobe) {
            log(ioobe.getMessage());
        }
        return body;
    }

    public String secondsToTime(int seconds) {
        int hours = seconds / 3600;
        seconds -= hours * 3600;
        int minutes = seconds / 60;
        seconds -= minutes * 60;
        int secs = seconds % 60;
        String secString = (secs < 10) ? "0" + secs : "" + secs;
        String minString = (minutes < 10) ? "0" + minutes : "" + minutes;
        return hours + ":" + minString + ":" + secString;
    }

    public int timeToSeconds(String time) {
        int seconds = Integer.valueOf(time.substring(time.length() - 2));
        int minutes = Integer.valueOf(time.substring(0, 2));
        return (minutes * 60) + seconds;
    }

    public AlertDialog.Builder createAlertDialogWithTitleAndMessage(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message);
        return builder;
    }

    public String formatStringForDatabase(String rawString) {
        rawString = rawString.replace(' ', '_');
        rawString = rawString.replace('.', '\0');
        rawString = rawString.replace('\'', '\0');
        rawString = rawString.replace('"', '\0');
        return rawString;
    }

    public int getResourceIdFromName(String name, Class<?> c) {
        int resourceId = 0;
        try {
            Field idField = c.getDeclaredField(name);
            resourceId = idField.getInt(idField);
        } catch (Exception e) {
            Util.getInstance().error(e.getMessage());
            resourceId = -1;
        }

        return resourceId;
    }

    public void closeKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();

        if (view == null) {
            view = new View(activity);
        }

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public String wrap(String str) {
        return "'" + str + "'";
    }

    public String wrap(int i) {
        return "'" + i + "'";
    }

    public void log(String message) {
        Log.e("INFO", message);
    }

    public void warn(String message) {
        Log.e("WARN", message);
    }

    public void error(String message) {
        Log.e("ERROR", message);
    }

    public boolean isNumber(Object obj) {
        try {
            CharSequence seq = obj.toString();
            return Globals.getInstance().REGEX_NUMBER.matcher(seq).matches();
        } catch (Exception e) {
            error("Object could not be converted to char sequence");
            return false;
        }
    }

    public boolean isString(Object obj) {
        try {
            boolean isValid = obj != null;
            isValid = isValid && obj.toString().trim().length() >= 0;
            return isValid;
        } catch (Exception e) {
            error("Object is not a string");
            return false;
        }
    }
}
