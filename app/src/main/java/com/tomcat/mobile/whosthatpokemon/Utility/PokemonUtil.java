package com.tomcat.mobile.whosthatpokemon.Utility;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.tomcat.mobile.whosthatpokemon.DataTables.PokemonData;
import com.tomcat.mobile.whosthatpokemon.DataTables.PokemonFamiliesData;
import com.tomcat.mobile.whosthatpokemon.Modules.Pokemon;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.Matcher;

public class PokemonUtil {
    private DataAccessHelper dataHelper;

    private SQLiteDatabase database;

    public PokemonUtil() {
        dataHelper = DataAccessHelper.getInstance();
    }

    public void open() throws SQLException {
        database = dataHelper.getWritableDatabase();
    }

    public void close() {
        dataHelper.close();
    }

    public void addPokemon(ContentValues cv) {
        open();
        try {
            database.insertOrThrow(PokemonData.TABLE_NAME, null, cv);
        } catch (SQLException sqle) {
            Util.getInstance().error(sqle.getLocalizedMessage());
        }

        close();
    }

    public Pokemon getPokemonBasedOnNumber(int number) {
        open();
        Cursor cursor = database.query(PokemonData.TABLE_NAME, PokemonData.COLUMNS, Util.getInstance().getWhereClauseWhereKeyIsValue(PokemonData.COLUMN_NUMBER, number), null, null, null, null);
        cursor.moveToFirst();
        Pokemon pokemon = parsePokemon(cursor);
        cursor.close();
        close();
        return pokemon;
    }

    public void logAllPokemon() {
        try {
            open();
            String query = "SELECT * FROM " + PokemonData.TABLE_NAME;
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                Util.getInstance().log(parsePokemon(cursor).toString());
                cursor.moveToNext();
            }
            cursor.close();
            close();
        } catch (SQLException sqle) {
            close();
        }
    }
    
    public ArrayList<Pokemon> getPokemonBasedOnName(String name) {
        ArrayList<Pokemon> pokemons = new ArrayList<>();
        try {
            open();
            String query = "SELECT * FROM " + PokemonData.TABLE_NAME + " WHERE " + Util.getInstance().getWhereClauseWhereKeyIsValue(PokemonData.COLUMN_NAME, name);
            Util.getInstance().log(query);
            Cursor cursor = database.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Pokemon p = parsePokemon(cursor);
                    pokemons.add(p);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        } catch (SQLException sqle) {
            Util.getInstance().error(sqle.getMessage());
        } finally {
            close();
        }

        return pokemons;
    }

    public Pokemon getRandomPokemonFromGeneration(int generation) {
        open();
        Cursor cursor = database.query(PokemonData.TABLE_NAME, PokemonData.COLUMNS, Util.getInstance().getWhereClauseWhereKeyIsValue(PokemonData.COLUMN_GENERATION, generation), null, null, null, "RANDOM()");
        cursor.moveToFirst();
        Pokemon pokemon = parsePokemon(cursor);
        cursor.close();
        close();
        return pokemon;
    }

    public Pokemon getRandomPokemon() {
        open();
        Cursor cursor = database.query(PokemonData.TABLE_NAME, PokemonData.COLUMNS, null, null, null, null, "RANDOM()");
        cursor.moveToFirst();
        Pokemon pokemon = parsePokemon(cursor);
        cursor.close();
        close();
        return pokemon;
    }

    public Pokemon[] getPokemonFamilyMembersForPokemon(int number) {
        Pokemon[] pokemons;
        open();
        String getPokemonFamilyStatement = "SELECT * FROM " + PokemonData.TABLE_NAME + " WHERE " +
                PokemonData.COLUMN_FAMILY_ID + " in (SELECT " + PokemonFamiliesData.COLUMN_FAMILY_ID + " FROM " +
                PokemonFamiliesData.TABLE_NAME + " WHERE " + PokemonFamiliesData.COLUMN_NUMBER + " = " +
                Util.getInstance().wrap(number) + ")";
        Util.getInstance().log(getPokemonFamilyStatement);

        try {
            Cursor cursor = database.rawQuery(getPokemonFamilyStatement, null);
            cursor.moveToFirst();
            pokemons = new Pokemon[cursor.getCount()];

            for (int i = 0; i < pokemons.length; i++) {
                pokemons[i] = parsePokemon(cursor);
                cursor.moveToNext();
            }
        } catch (SQLException sqle) {
            Util.getInstance().error(sqle.getMessage());
            pokemons = null;
        } finally {
            close();
        }

        return pokemons;
    }

    public Pokemon[] getTopNPokemonFromNameSearch(int n, String search) {
        Pokemon[] pokemons;
        try {
            open();
            String query = "SELECT * FROM " + PokemonData.TABLE_NAME + " WHERE " + PokemonData.COLUMN_NAME +
                    " LIKE " + "'%" + search + "%' LIMIT " + n;
            Util.getInstance().log(query);
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            pokemons = new Pokemon[cursor.getCount()];

            for (int i = 0; i < pokemons.length; i++) {
                pokemons[i] = parsePokemon(cursor);
                cursor.moveToNext();
            }
            cursor.close();

        } catch (IndexOutOfBoundsException ioobe) {
            Util.getInstance().error(ioobe.getMessage());
            pokemons = null;
        } finally {
            close();
        }

        return pokemons;
    }

    public int getFamilyMemberCountForPokemon(int number) {
        int memberCount = 0;
        open();
        String getCountStatement = "SELECT COUNT(*) FROM " + PokemonFamiliesData.TABLE_NAME + " WHERE " +
                PokemonFamiliesData.COLUMN_FAMILY_ID + " = (SELECT " + PokemonFamiliesData.COLUMN_FAMILY_ID +
                " FROM " + PokemonFamiliesData.TABLE_NAME + " WHERE " + PokemonFamiliesData.COLUMN_NUMBER + " = " +
                Util.getInstance().wrap(number) + ")";

        Util.getInstance().log(getCountStatement);
        try {
            Cursor cursor = database.rawQuery(getCountStatement, null);
            cursor.moveToFirst();
            memberCount = cursor.getInt(0);
            cursor.close();
        } catch (SQLException sqle) {
            Util.getInstance().error(sqle.getMessage());
        } finally {
            close();
        }

        return memberCount;
    }

    public void deletePokemon(int number) {
        open();
        database.delete(PokemonData.TABLE_NAME, Util.getInstance().getWhereClauseWhereKeyIsValue(PokemonData.COLUMN_NUMBER, number), null);
        close();
    }

    public boolean updatePokemon(ContentValues cv) {
        open();
        int number = cv.getAsInteger(PokemonData.COLUMN_NUMBER);
        boolean isUpdated = database.update(PokemonData.TABLE_NAME, cv, Util.getInstance().getWhereClauseWhereKeyIsValue(PokemonData.COLUMN_NUMBER, number), null) > 0;
        close();
        return isUpdated;
    }
    
    public Pokemon parsePokemon(Cursor cursor) {
        Pokemon pokemon = new Pokemon();
        
        pokemon.setNumber(cursor.getInt(0));
        pokemon.setName(cursor.getString(1));
        pokemon.setGeneration(cursor.getInt(2));
        pokemon.setHeight(cursor.getDouble(3));
        pokemon.setWeight(cursor.getDouble(4));
        pokemon.setHp(cursor.getInt(5));
        pokemon.setAttack(cursor.getInt(6));
        pokemon.setDefense(cursor.getInt(7));
        pokemon.setSpAtk(cursor.getInt(8));
        pokemon.setSpDef(cursor.getInt(9));
        pokemon.setSpeed(cursor.getInt(10));
        pokemon.setFamilyId(cursor.getInt(11));
        pokemon.setEvoNum(cursor.getInt(12));
        pokemon.setColor(cursor.getString(13));
        pokemon.setIsRegionalVariant(cursor.getInt(14));

        return pokemon;
    }

    public boolean isPokemonValid(Pokemon pokemon) {
        boolean isValid = true;

        isValid = isValid && Util.getInstance().isNumber(pokemon.getNumber());
        isValid = isValid && Util.getInstance().isString(pokemon.getName());
        isValid = isValid && Util.getInstance().isNumber(pokemon.getGeneration());
        isValid = isValid && Util.getInstance().isNumber(pokemon.getHeight());
        isValid = isValid && Util.getInstance().isNumber(pokemon.getWeight());
        isValid = isValid && Util.getInstance().isNumber(pokemon.getHp());
        isValid = isValid && Util.getInstance().isNumber(pokemon.getAttack());
        isValid = isValid && Util.getInstance().isNumber(pokemon.getDefense());
        isValid = isValid && Util.getInstance().isNumber(pokemon.getSpAtk());
        isValid = isValid && Util.getInstance().isNumber(pokemon.getSpDef());
        isValid = isValid && Util.getInstance().isNumber(pokemon.getSpeed());
        isValid = isValid && Util.getInstance().isNumber(pokemon.getFamilyId());
        isValid = isValid && Util.getInstance().isNumber(pokemon.getEvoNum());
        isValid = isValid && Util.getInstance().isString(pokemon.getColor());

        return isValid;
    }
}