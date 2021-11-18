package com.tomcat.mobile.whosthatpokemon.Utility;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tomcat.mobile.whosthatpokemon.DataTables.PokemonTypesData;
import com.tomcat.mobile.whosthatpokemon.DataTables.TypeData;
import com.tomcat.mobile.whosthatpokemon.Modules.PokemonTypes;
import com.tomcat.mobile.whosthatpokemon.Modules.Type;

public class PokemonTypesUtil {
    private DataAccessHelper dataHelper;

    private SQLiteDatabase database;

    public PokemonTypesUtil() {
        dataHelper = DataAccessHelper.getInstance();
    }

    public void open() throws SQLException {
        database = dataHelper.getWritableDatabase();
    }

    public void close() {
        dataHelper.close();
    }

    public void addPokemonType(ContentValues cv) {
        open();
        try {
            database.insertOrThrow(PokemonTypesData.TABLE_NAME, null, cv);
        } catch (SQLException sqle) {
            Log.e("ERROR", sqle.getLocalizedMessage());
        }

        close();
    }

    public PokemonTypes[] getPokemonTypes(int number) {
        open();
        PokemonTypes[] type;
        String query = "SELECT * FROM " + PokemonTypesData.TABLE_NAME + " WHERE " + PokemonTypesData.COLUMN_NUMBER + " = " + Util.getInstance().wrap(number);
        Util.getInstance().log("Getting types with Statement: " + query);
        Cursor cursor = database.rawQuery(query, null);
        type = new PokemonTypes[cursor.getCount()];
        try {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                type[i] = parseType(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (CursorIndexOutOfBoundsException cioobe) {
            Util.getInstance().error(cioobe.getMessage());
        }

        close();
        return type;
    }

    public PokemonTypes parseType(Cursor cursor) {
        PokemonTypes type = new PokemonTypes();

        type.setNumber(cursor.getInt(0));
        String t = cursor.getString(1);
        t = (t.equals("Empty")) ? "No Type" : t;
        type.setType(t);

        return type;
    }
}
