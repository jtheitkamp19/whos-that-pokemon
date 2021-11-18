package com.tomcat.mobile.whosthatpokemon.DataTables;

public class MoveData {
    public final static String TABLE_NAME = "Moves";

    public final static String COLUMN_ID = "id";
    public final static String COLUMN_NAME = "name";
    public final static String COLUMN_ACCURACY = "accuracy";
    public final static String COLUMN_DAMAGE_CLASS = "damageclass";
    public final static String COLUMN_GENERATION = "generation";
    public final static String COLUMN_TYPE = "type";
    public final static String COLUMN_PP = "pp";
    public final static String COLUMN_POWER = "power";

    public final static String[] COLUMNS = {COLUMN_ID, COLUMN_NAME, COLUMN_ACCURACY, COLUMN_DAMAGE_CLASS, COLUMN_GENERATION,
        COLUMN_TYPE, COLUMN_PP, COLUMN_POWER};
    public final static String[] DATA_TYPES = {"integer primary key", "text not null", "integer not null", "text not null",
        "integer not null", "text not null", "integer not null", "integer not null"};

    public static String getDatabaseTableCreationStatement() {
        String tableCreationStatement = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(";
        for (int i = 0; i < COLUMNS.length; i++) {
            if (i != 0) {
                tableCreationStatement += ", ";
            }
            tableCreationStatement += COLUMNS[ i ] + " " + DATA_TYPES[ i ];
        }
        tableCreationStatement += ");";
        return tableCreationStatement;
    }
}
