package com.tomcat.mobile.whosthatpokemon.DataTables;

public class PokemonAbilityData {
    public final static String TABLE_NAME = "PokemonAbilities";

    public final static String COLUMN_NUMBER = "number";
    public final static String COLUMN_ABILITY = "ability";

    public final static String[] COLUMNS = {COLUMN_NUMBER, COLUMN_ABILITY};
    public final static String[] DATA_TYPES = {"integer not null", "text not null"};

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
