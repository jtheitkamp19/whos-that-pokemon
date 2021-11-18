package com.tomcat.mobile.whosthatpokemon.Modules;

import android.content.ContentValues;
import android.util.Log;

import com.tomcat.mobile.whosthatpokemon.DataTables.PokemonAbilityData;
import com.tomcat.mobile.whosthatpokemon.DataTables.PokemonFamiliesData;

import org.json.JSONException;
import org.json.JSONObject;

public class PokemonAbility {
    private int number = 0;
    private String ability = "";

    public PokemonAbility() {

    }

    public PokemonAbility(JSONObject jsonObject) {
        try {
            number = jsonObject.getInt(PokemonAbilityData.COLUMN_NUMBER);
            ability = jsonObject.getString(PokemonAbilityData.COLUMN_ABILITY);
        } catch (JSONException jsone) {
            Log.e("ERROR", jsone.getMessage());
        }
    }

    public int getNumber() {
        return number;
    }

    public String getAbility() {
        return ability;
    }

    public void setNumber(int i) {
        number = i;
    }

    public void setAbility(String s) {
        ability = s;
    }

    public ContentValues getContentValuesForType() {
        ContentValues cv = new ContentValues();

        cv.put(PokemonAbilityData.COLUMN_NUMBER, number);
        cv.put(PokemonAbilityData.COLUMN_ABILITY, ability);

        return cv;
    }
}
