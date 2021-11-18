package com.tomcat.mobile.whosthatpokemon.DataTables;

public abstract class DataTable {
    String DATABASE_NAME = "pokemondb.db";
    int DATABASE_VERSION = 1;
    protected String[] COLUMNS;
    protected String[] DATA_TYPES;
    protected String TABLE_NAME;



    public String[] getColumns() {
        return COLUMNS;
    }

    public String getTableName() {
        return TABLE_NAME;
    }
}
