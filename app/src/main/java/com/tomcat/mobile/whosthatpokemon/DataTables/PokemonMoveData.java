package com.tomcat.mobile.whosthatpokemon.DataTables;

public class PokemonMoveData {
    public final static String TABLE_NAME = "PokemonMoves";

    public final static String COLUMN_NUMBER = "number";
    public final static String COLUMN_MOVE = "moveid";

    public final static String[] COLUMNS = {COLUMN_NUMBER, COLUMN_MOVE};
    public final static String[] DATA_TYPES = {"integer not null", "integer not null"};

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
