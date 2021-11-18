package com.tomcat.mobile.whosthatpokemon.DataTables;

public class PokemonData extends DataTable {
    public final static String TABLE_NAME = "Pokemon";

    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_GENERATION = "generation";
    public static final String COLUMN_HEIGHT = "height";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_HP = "hp";
    public static final String COLUMN_ATTACK = "attack";
    public static final String COLUMN_DEFENSE = "defense";
    public static final String COLUMN_SP_ATK = "spatk";
    public static final String COLUMN_SP_DEF = "spdef";
    public static final String COLUMN_SPEED = "speed";
    public static final String COLUMN_FAMILY_ID = "familyid";
    public static final String COLUMN_EVO_NUM = "evonum";
    public static final String COLUMN_COLOR = "color";
    public static final String COLUMN_TYPES = "types";

    public static final String POKEMON_TYPE_ID = "id";
    public static final String POKEMON_TYPE_NAME = "type";

    public static String[] COLUMNS = {COLUMN_NUMBER, COLUMN_NAME, COLUMN_GENERATION, COLUMN_HEIGHT, COLUMN_WEIGHT,
        COLUMN_HP, COLUMN_ATTACK, COLUMN_DEFENSE, COLUMN_SP_ATK, COLUMN_SP_DEF, COLUMN_SPEED, COLUMN_FAMILY_ID,
        COLUMN_EVO_NUM, COLUMN_COLOR, COLUMN_TYPES};
    public static String[] DATA_TYPES = {"integer primary key", "text not null", "int not null", "double not null", "double not null",
        "int not null", "int not null", "int not null", "int not null", "int not null", "int not null", "int not null",
        "int not null", "text not null", "text not null"};

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
