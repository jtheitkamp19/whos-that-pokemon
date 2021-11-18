package com.tomcat.mobile.whosthatpokemon.Modules;

import android.content.ContentValues;
import android.util.Log;

import com.tomcat.mobile.whosthatpokemon.DataTables.TypeData;

import org.json.JSONException;
import org.json.JSONObject;

public class Type {
    private String type = "";

    public Type() {

    }

    public Type(JSONObject jsonObject) {
        try {
            type = jsonObject.getString(TypeData.COLUMN_TYPE);
        } catch (JSONException jsone) {
            Log.e("ERROR", jsone.getMessage());
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String s) {
        type = s;
    }

    public ContentValues getContentValuesForType() {
        ContentValues cv = new ContentValues();

        cv.put(TypeData.COLUMN_TYPE, type);

        return cv;
    }
}