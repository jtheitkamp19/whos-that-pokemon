package com.tomcat.mobile.whosthatpokemon.Utility;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tomcat.mobile.whosthatpokemon.DataTables.MoveData;
import com.tomcat.mobile.whosthatpokemon.DataTables.PokemonAbilityData;
import com.tomcat.mobile.whosthatpokemon.DataTables.PokemonData;
import com.tomcat.mobile.whosthatpokemon.DataTables.PokemonFamiliesData;
import com.tomcat.mobile.whosthatpokemon.DataTables.PokemonMoveData;
import com.tomcat.mobile.whosthatpokemon.DataTables.PokemonTypesData;
import com.tomcat.mobile.whosthatpokemon.DataTables.StatData;
import com.tomcat.mobile.whosthatpokemon.DataTables.TypeData;
import com.tomcat.mobile.whosthatpokemon.Modules.PokemonAbility;

import java.util.ArrayList;

public class DataAccessHelper extends SQLiteOpenHelper {
    private static DataAccessHelper dah = null;
    private static final String DATABASE_NAME = "pokemondb.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DB_TABLE_POKEMON = "Pokemon";
    private static final String DB_TABLE_POKEMON_TYPES = "PokemonTypes";
    private static final String DB_TABLE_TYPES = "Types";
    private static final String DB_TABLE_POKEMON_FAMILIES = "PokemonFamilies";
    private static final String DB_TABLE_STATS = StatData.TABLE_NAME;
    private static final String DB_TABLE_MOVES = MoveData.TABLE_NAME;
    private static final String DB_TABLE_POKEMON_MOVES = PokemonMoveData.TABLE_NAME;
    private static final String DB_TABLE_POKEMON_ABILITIES = PokemonAbilityData.TABLE_NAME;

    private ArrayList<String> creationStatements = new ArrayList<>();

    private DataAccessHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        creationStatements.add(PokemonData.getDatabaseTableCreationStatement());
        creationStatements.add(TypeData.getDatabaseTableCreationStatement());
        creationStatements.add(PokemonTypesData.getDatabaseTableCreationStatement());
        creationStatements.add(PokemonFamiliesData.getDatabaseTableCreationStatement());
        creationStatements.add(StatData.getDatabaseTableCreationStatement());
        creationStatements.add(MoveData.getDatabaseTableCreationStatement());
        creationStatements.add(PokemonAbilityData.getDatabaseTableCreationStatement());
        creationStatements.add(PokemonMoveData.getDatabaseTableCreationStatement());
        onCreate(this.getWritableDatabase());
    }

    public static void setInstance(Context context) {
        if (dah == null) {
            dah = new DataAccessHelper(context);
        }
    }

    public static synchronized DataAccessHelper getInstance() {
        return dah;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        if (!Util.getInstance().getPropertyHasLocalData()) {
            for (int i = 0; i < creationStatements.size(); i++) {
                Util.getInstance().log(creationStatements.get(i));
                database.execSQL(creationStatements.get(i));
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        resetLocalData(database);
        onCreate(database);
    }

    @Override
    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    public void resetStats(SQLiteDatabase database) {
        final String DROP_TABLE_STATEMENT = "DROP TABLE IF EXISTS ";
        database.execSQL(DROP_TABLE_STATEMENT + DB_TABLE_STATS);
        database.execSQL(StatData.getDatabaseTableCreationStatement()); }


    public void resetLocalData(SQLiteDatabase database) {
        final String DROP_TABLE_STATEMENT = "DROP TABLE IF EXISTS ";
        database.execSQL(DROP_TABLE_STATEMENT + DB_TABLE_POKEMON);
        database.execSQL(DROP_TABLE_STATEMENT + DB_TABLE_POKEMON_TYPES);
        database.execSQL(DROP_TABLE_STATEMENT + DB_TABLE_TYPES);
        database.execSQL(DROP_TABLE_STATEMENT + DB_TABLE_POKEMON_FAMILIES);
        database.execSQL(DROP_TABLE_STATEMENT + DB_TABLE_MOVES);
        database.execSQL(DROP_TABLE_STATEMENT + DB_TABLE_POKEMON_ABILITIES);
        database.execSQL(DROP_TABLE_STATEMENT + DB_TABLE_POKEMON_MOVES);
        Util.getInstance().setPropertyHasLocalData(false);
    }
}
