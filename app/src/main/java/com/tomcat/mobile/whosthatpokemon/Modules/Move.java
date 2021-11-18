package com.tomcat.mobile.whosthatpokemon.Modules;

import android.content.ContentValues;
import android.util.Log;

import com.tomcat.mobile.whosthatpokemon.DataTables.MoveData;

import org.json.JSONException;
import org.json.JSONObject;

public class Move {
    private int id = 0;
    private String name = "";
    private int accuracy = 0;
    private String damageclass = "";
    private int generation = 0;
    private String type = "";
    private int pp = 0;
    private int power = 0;

    public Move() {

    }

    public Move(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt(MoveData.COLUMN_ID);
            name = jsonObject.getString(MoveData.COLUMN_NAME);
            accuracy = jsonObject.getInt(MoveData.COLUMN_ACCURACY);
            damageclass = jsonObject.getString(MoveData.COLUMN_DAMAGE_CLASS);
            generation = jsonObject.getInt(MoveData.COLUMN_GENERATION);
            type = jsonObject.getString(MoveData.COLUMN_TYPE);
            pp = jsonObject.getInt(MoveData.COLUMN_PP);
            power = jsonObject.getInt(MoveData.COLUMN_POWER);
        } catch (JSONException jsone) {
            Log.e("ERROR", jsone.getMessage());
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public String getDamageclass() {
        return damageclass;
    }

    public int getGeneration() {
        return generation;
    }

    public String getType() {
        return type;
    }

    public int getPp() {
        return pp;
    }

    public int getPower() {
        return power;
    }

    public void setId(int i) {
        id = i;
    }

    public void setName(String s) {
        name = s;
    }

    public void setAccuracy(int i) {
        accuracy = i;
    }

    public void setDamageclass(String s) {
        damageclass = s;
    }

    public void setGeneration(int i) {
        generation = i;
    }

    public void setType(String s) {
        type = s;
    }

    public void setPp(int i) {
        pp = i;
    }

    public void setPower(int i) {
        power = i;
    }

    public ContentValues getContentValuesForType() {
        ContentValues cv = new ContentValues();

        cv.put(MoveData.COLUMN_ID, id);
        cv.put(MoveData.COLUMN_NAME, name);
        cv.put(MoveData.COLUMN_ACCURACY, accuracy);
        cv.put(MoveData.COLUMN_DAMAGE_CLASS, damageclass);
        cv.put(MoveData.COLUMN_GENERATION, generation);
        cv.put(MoveData.COLUMN_TYPE, type);
        cv.put(MoveData.COLUMN_PP, pp);
        cv.put(MoveData.COLUMN_POWER, power);

        return cv;
    }
}
