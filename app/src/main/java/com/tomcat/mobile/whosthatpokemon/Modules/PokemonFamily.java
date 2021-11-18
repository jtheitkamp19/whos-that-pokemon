package com.tomcat.mobile.whosthatpokemon.Modules;

import android.content.ContentValues;
import android.util.Log;

import com.tomcat.mobile.whosthatpokemon.DataTables.PokemonFamiliesData;
import com.tomcat.mobile.whosthatpokemon.DataTables.PokemonTypesData;

import org.json.JSONException;
import org.json.JSONObject;

public class PokemonFamily {
    private int number = 0;
    private int familyid = 0;

    public PokemonFamily() {

    }

    public PokemonFamily(JSONObject jsonObject) {
        try {
            number = jsonObject.getInt(PokemonFamiliesData.COLUMN_NUMBER);
            familyid = jsonObject.getInt(PokemonFamiliesData.COLUMN_FAMILY_ID);
        } catch (JSONException jsone) {
            Log.e("ERROR", jsone.getMessage());
        }
    }

    public int getNumber() {
        return number;
    }

    public int getFamilyId() {
        return familyid;
    }

    public void setNumber(int i) {
        number = i;
    }

    public void setFamilyId(int i) {
        familyid = i;
    }

    public ContentValues getContentValuesForType() {
        ContentValues cv = new ContentValues();

        cv.put(PokemonFamiliesData.COLUMN_NUMBER, number);
        cv.put(PokemonFamiliesData.COLUMN_FAMILY_ID, familyid);

        return cv;
    }
}
