package com.tomcat.mobile.whosthatpokemon.Utility;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.tomcat.mobile.whosthatpokemon.DataTables.PokemonFamiliesData;
import com.tomcat.mobile.whosthatpokemon.DataTables.PokemonMoveData;
import com.tomcat.mobile.whosthatpokemon.Modules.PokemonFamily;
import com.tomcat.mobile.whosthatpokemon.Modules.PokemonMove;

public class PokemonMoveUtil {
    private DataAccessHelper dataHelper;

    private SQLiteDatabase database;

    public PokemonMoveUtil() {
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
            database.insertOrThrow(PokemonMoveData.TABLE_NAME, null, cv);
        } catch (SQLException sqle) {
            Util.getInstance().error(sqle.getLocalizedMessage());
        }

        close();
    }

    public PokemonMove parseType(Cursor cursor) {
        PokemonMove move = new PokemonMove();

        move.setNumber(cursor.getInt(0));
        move.setMove(cursor.getInt(1));

        return move;
    }
}
