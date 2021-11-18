package com.tomcat.mobile.whosthatpokemon.DataTables;

public class PokemonFamiliesData {
    public static String TABLE_NAME = "PokemonFamilies";

    public static String COLUMN_NUMBER = "number";
    public static String COLUMN_FAMILY_ID = "familyid";

    private static final String[] COLUMNS = {COLUMN_NUMBER, COLUMN_FAMILY_ID};
    private static final String[] DATA_TYPES = {"integer not null", "integer not null"};

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
