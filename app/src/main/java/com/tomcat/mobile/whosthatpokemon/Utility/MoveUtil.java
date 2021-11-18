package com.tomcat.mobile.whosthatpokemon.Utility;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tomcat.mobile.whosthatpokemon.DataTables.MoveData;
import com.tomcat.mobile.whosthatpokemon.DataTables.TypeData;
import com.tomcat.mobile.whosthatpokemon.Modules.Move;
import com.tomcat.mobile.whosthatpokemon.Modules.Type;

public class MoveUtil {
    private DataAccessHelper dataHelper;

    private SQLiteDatabase database;

    public MoveUtil() {
        dataHelper = DataAccessHelper.getInstance();
    }

    public void open() throws SQLException {
        database = dataHelper.getWritableDatabase();
    }

    public void close() {
        dataHelper.close();
    }

    public void addMove(ContentValues cv) {
        open();
        try {
            database.insertOrThrow(MoveData.TABLE_NAME, null, cv);
        } catch (SQLException sqle) {
            Log.e("ERROR", sqle.getLocalizedMessage());
        }

        close();
    }

    public Move parseMove(Cursor cursor) {
        Move move = new Move();

        move.setId(cursor.getInt(0));
        move.setName(cursor.getString(1));
        move.setAccuracy(cursor.getInt(2));
        move.setDamageclass(cursor.getString(3));
        move.setGeneration(cursor.getInt(4));
        move.setType(cursor.getString(5));
        move.setPp(cursor.getInt(6));
        move.setPower(cursor.getInt(7));

        return move;
    }
}
