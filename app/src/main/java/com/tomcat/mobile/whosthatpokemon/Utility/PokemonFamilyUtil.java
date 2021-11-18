package com.tomcat.mobile.whosthatpokemon.Utility;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.tomcat.mobile.whosthatpokemon.DataTables.PokemonFamiliesData;
import com.tomcat.mobile.whosthatpokemon.DataTables.PokemonTypesData;
import com.tomcat.mobile.whosthatpokemon.Modules.PokemonFamily;
import com.tomcat.mobile.whosthatpokemon.Modules.PokemonTypes;

public class PokemonFamilyUtil {
    private DataAccessHelper dataHelper;

    private SQLiteDatabase database;

    public PokemonFamilyUtil() {
        dataHelper = DataAccessHelper.getInstance();
    }

    public void open() throws SQLException {
        database = dataHelper.getWritableDatabase();
    }

    public void close() {
        dataHelper.close();
    }

    public void addPokemonFamily(ContentValues cv) {
        open();
        try {
            database.insertOrThrow(PokemonFamiliesData.TABLE_NAME, null, cv);
        } catch (SQLException sqle) {
            Util.getInstance().error(sqle.getLocalizedMessage());
        }

        close();
    }

    public PokemonFamily parseFamily(Cursor cursor) {
        PokemonFamily family = new PokemonFamily();

        family.setNumber(cursor.getInt(0));
        family.setFamilyId(cursor.getInt(1));

        return family;
    }
}
