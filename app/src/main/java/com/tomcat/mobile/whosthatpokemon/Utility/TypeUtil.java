package com.tomcat.mobile.whosthatpokemon.Utility;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tomcat.mobile.whosthatpokemon.DataTables.TypeData;
import com.tomcat.mobile.whosthatpokemon.Modules.Type;

import java.util.ArrayList;
import java.util.List;

public class TypeUtil {
    private DataAccessHelper dataHelper;
    private final String TYPE_SEPARATOR = ";";
    private final String TYPE_FIELD_SEPARATOR = ":";

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

    public List<Type> getTypesFromString(String typeString) {
        String[] typeSections = typeString.split(TYPE_SEPARATOR);
        List<Type> types = new ArrayList<>();

        for(String typeData : typeSections) {
            String[] typeFieldData = typeData.split(TYPE_FIELD_SEPARATOR);

            if (typeFieldData.length == 2) {
                types.add(new Type(Integer.parseInt(typeFieldData[0]), typeFieldData[1]));
            }
        }

        return types;
    }

    public String getMultiTypeString(List<Type> typeData) {
        String typeString = "";

        for (Type t : typeData) {
            if (typeString.isEmpty()) {
                typeString += t.getId() + TYPE_FIELD_SEPARATOR + t.getType();
            } else {
                typeString += TYPE_SEPARATOR + t.getId() + TYPE_FIELD_SEPARATOR + t.getType();
            }
        }

        return typeString;
    }
}
