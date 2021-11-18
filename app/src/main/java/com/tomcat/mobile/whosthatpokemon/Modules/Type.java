package com.tomcat.mobile.whosthatpokemon.Modules;

import android.content.ContentValues;
import android.util.Log;

import com.tomcat.mobile.whosthatpokemon.DataTables.TypeData;

import org.json.JSONException;
import org.json.JSONObject;

public class Type {
    private int id = 0;
    private String type = "";

    public Type() {

    }

    public Type(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt(TypeData.COLUMN_ID);
            type = jsonObject.getString(TypeData.COLUMN_TYPE);
        } catch (JSONException jsone) {
            Log.e("ERROR", jsone.getMessage());
        }
    }

    public Type(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }
    public String getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setType(String s) {
        type = s;
    }

    public ContentValues getContentValuesForType() {
        ContentValues cv = new ContentValues();

        cv.put(TypeData.COLUMN_ID, id);
        cv.put(TypeData.COLUMN_TYPE, type);

        return cv;
    }
}