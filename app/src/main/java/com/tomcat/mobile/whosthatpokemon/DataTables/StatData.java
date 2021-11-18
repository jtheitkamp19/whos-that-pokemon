package com.tomcat.mobile.whosthatpokemon.DataTables;

public class StatData extends DataTable {
    public static final String TABLE_NAME = "Stats";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_POKEMON_ID = "pokemonid";
    public static final String COLUMN_GAME_TIME = "gametime";
    public static final String COLUMN_GUESSES = "guesses";
    public static final String COLUMN_VALID_GUESSES = "validguesses";
    public static final String COLUMN_DIFFICULTY = "difficulty";
    public static final String COLUMN_WON_GAME = "wongame";

    public static final String[] COLUMNS = {COLUMN_ID, COLUMN_POKEMON_ID, COLUMN_GAME_TIME, COLUMN_GUESSES, COLUMN_VALID_GUESSES,
        COLUMN_DIFFICULTY, COLUMN_WON_GAME};
    public static final String[] DATA_TYPES = {"integer primary key autoincrement", "int not null", "int not null", "int not null",
            "int not null", "int not null", "int not null"};

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
