package com.tomcat.mobile.whosthatpokemon.Modules;

import android.content.ContentValues;

import com.tomcat.mobile.whosthatpokemon.DataTables.PokemonTypesData;
import com.tomcat.mobile.whosthatpokemon.DataTables.TypeData;
import com.tomcat.mobile.whosthatpokemon.Utility.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class PokemonTypes {
    private int number = 0;
    private String type = "";

    public PokemonTypes() {

    }

    public PokemonTypes(JSONObject jsonObject) {
        try {
            number = jsonObject.getInt(PokemonTypesData.COLUMN_NUMBER);
            type = jsonObject.getString(PokemonTypesData.COLUMN_TYPE);
        } catch (JSONException jsone) {
            Util.getInstance().error(jsone.getMessage());
        }
    }

    public int getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public void setNumber(int i) {
        number = i;
    }

    public void setType(String s) {
        type = s;
    }

    public ContentValues getContentValuesForType() {
        ContentValues cv = new ContentValues();

        cv.put(PokemonTypesData.COLUMN_NUMBER, number);
        cv.put(PokemonTypesData.COLUMN_TYPE, type);

        return cv;
    }
}
