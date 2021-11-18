package com.tomcat.mobile.whosthatpokemon.Utility;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.tomcat.mobile.whosthatpokemon.DataTables.PokemonAbilityData;
import com.tomcat.mobile.whosthatpokemon.Modules.PokemonAbility;

public class PokemonAbilityUtil {
    private DataAccessHelper dataHelper;

    private SQLiteDatabase database;

    public PokemonAbilityUtil() {
        dataHelper = DataAccessHelper.getInstance();
    }

    public void open() throws SQLException {
        database = dataHelper.getWritableDatabase();
    }

    public void close() {
        dataHelper.close();
    }

    public void addPokemonAbility(ContentValues cv) {
        open();
        try {
            database.insertOrThrow(PokemonAbilityData.TABLE_NAME, null, cv);
        } catch (SQLException sqle) {
            Util.getInstance().error(sqle.getLocalizedMessage());
        }

        close();
    }

    public PokemonAbility parseType(Cursor cursor) {
        PokemonAbility ability = new PokemonAbility();

        ability.setNumber(cursor.getInt(0));
        ability.setAbility(cursor.getString(1));

        return ability;
    }
}
