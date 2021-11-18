package com.tomcat.mobile.whosthatpokemon.Modules;

import android.content.ContentValues;

import com.tomcat.mobile.whosthatpokemon.DataTables.PokemonMoveData;
import com.tomcat.mobile.whosthatpokemon.DataTables.PokemonTypesData;
import com.tomcat.mobile.whosthatpokemon.Utility.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class PokemonMove {
    private int number = 0;
    private int move = 0;

    public PokemonMove() {

    }

    public PokemonMove(JSONObject jsonObject) {
        try {
            number = jsonObject.getInt(PokemonMoveData.COLUMN_NUMBER);
            move = jsonObject.getInt(PokemonMoveData.COLUMN_MOVE);
        } catch (JSONException jsone) {
            Util.getInstance().error(jsone.getMessage());
        }
    }

    public int getNumber() {
        return number;
    }

    public int getMove() {
        return move;
    }

    public void setNumber(int i) {
        number = i;
    }

    public void setMove(int i) {
        move = i;
    }

    public ContentValues getContentValuesForType() {
        ContentValues cv = new ContentValues();

        cv.put(PokemonMoveData.COLUMN_NUMBER, number);
        cv.put(PokemonMoveData.COLUMN_MOVE, move);

        return cv;
    }
}
