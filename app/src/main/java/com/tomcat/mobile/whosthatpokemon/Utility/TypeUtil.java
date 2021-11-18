package com.tomcat.mobile.whosthatpokemon.Utility;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tomcat.mobile.whosthatpokemon.DataTables.PokemonData;
import com.tomcat.mobile.whosthatpokemon.DataTables.PokemonTypesData;
import com.tomcat.mobile.whosthatpokemon.DataTables.TypeData;
import com.tomcat.mobile.whosthatpokemon.Modules.Pokemon;
import com.tomcat.mobile.whosthatpokemon.Modules.Type;

public class TypeUtil {
    private DataAccessHelper dataHelper;

    private SQLiteDatabase database;

    public TypeUtil() {
        dataHelper = DataAccessHelper.getInstance();
    }

    public void open() throws SQLException {
        database = dataHelper.getWritableDatabase();
    }

    public void close() {
        dataHelper.close();
    }

    public void addType(ContentValues cv) {
        open();
        try {
            database.insertOrThrow(TypeData.TABLE_NAME, null, cv);
        } catch (SQLException sqle) {
            Log.e("ERROR", sqle.getLocalizedMessage());
        }

        close();
    }

    public Type parseType(Cursor cursor) {
        Type type = new Type();

        type.setType(cursor.getString(0));

        return type;
    }
}
