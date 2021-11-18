package com.tomcat.mobile.whosthatpokemon.DataTables;

public class TypeData extends DataTable {
    public static final String TABLE_NAME = "Types";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE = "type";

    public static final String[] COLUMNS = {COLUMN_ID, COLUMN_TYPE};
    public static final String[] DATA_TYPES = {"integer primary key", "text not null"};

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
