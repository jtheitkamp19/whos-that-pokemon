package com.tomcat.mobile.whosthatpokemon.Utility;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tomcat.mobile.whosthatpokemon.DataTables.PokemonData;
import com.tomcat.mobile.whosthatpokemon.DataTables.StatData;
import com.tomcat.mobile.whosthatpokemon.DataTables.TypeData;
import com.tomcat.mobile.whosthatpokemon.Modules.Pokemon;
import com.tomcat.mobile.whosthatpokemon.Modules.Stats;
import com.tomcat.mobile.whosthatpokemon.Modules.Type;

public class StatUtil {
    private DataAccessHelper dataHelper;
    private String POKEMON_TABLE_ABBREVIATION = "p";

    private SQLiteDatabase database;

    public StatUtil() {
        dataHelper = DataAccessHelper.getInstance();
    }

    public void open() throws SQLException {
        database = dataHelper.getWritableDatabase();
    }

    public void close() {
        dataHelper.close();
    }

    public void addGameStats(ContentValues cv) {
        open();
        try {
            database.insertOrThrow(StatData.TABLE_NAME, null, cv);
        } catch (SQLException sqle) {
            Log.e("ERROR", sqle.getLocalizedMessage());
        }

        close();
    }

    public String getWhereClauseForDifficulty(int difficulty) {
        return " WHERE " + StatData.COLUMN_DIFFICULTY + " = " + Util.getInstance().wrap(difficulty);
    }

    public String getWhereClauseForPokemonGeneration(boolean isWhere, int generation) {
        String clause = (isWhere) ? " WHERE " : " AND ";
        clause += POKEMON_TABLE_ABBREVIATION + "." + PokemonData.COLUMN_GENERATION + " = " + Util.getInstance().wrap(generation);
        return clause;
    }

    public String getWhereClauseForWonGame(boolean isWhere) {
        String clause = (isWhere) ? " WHERE " : " AND ";
        clause += StatData.COLUMN_WON_GAME + " = " + Util.getInstance().wrap(Globals.getInstance().WON_GAME);
        return clause;
    }

    public String getJoinClauseForPokemonStats() {
        return " JOIN " + PokemonData.TABLE_NAME + " " + POKEMON_TABLE_ABBREVIATION + " ON " + StatData.TABLE_NAME + "." + StatData.COLUMN_POKEMON_ID + " = " + POKEMON_TABLE_ABBREVIATION + "." + PokemonData.COLUMN_NUMBER;
    }

    public int[] getMultiIntColumnValueFromQuery(String query) {
        Util.getInstance().log(query);
        int[] values;

        try {
            open();
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            values = new int[cursor.getColumnCount()];

            for (int i = 0; i < values.length; i++) {
                values[i] = cursor.getInt(i);
            }
            cursor.close();
        } catch (IndexOutOfBoundsException ioobe) {
            Util.getInstance().error(ioobe.getMessage());
            values = null;
        } catch (SQLException sqle) {
            Util.getInstance().error(sqle.getMessage());
            values = null;
        } finally {
            close();
        }

        return values;
    }

    public double getSingleColumnDoubleFromQuery(String query) {
        Util.getInstance().log(query);
        double value = 0;

        try {
            open();
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            value = cursor.getDouble(0);
            cursor.close();
        } catch (SQLException sqle) {
            Util.getInstance().error(sqle.getMessage());
        } catch (IndexOutOfBoundsException ioobe) {
            Util.getInstance().error(ioobe.getMessage());
        } finally {
            close();
        }

        return value;
    }

    public int getSingleColumnValueFromQuery(String query) {
        Util.getInstance().log(query);
        int value = 0;

        try {
            open();
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            value = cursor.getInt(0);
            cursor.close();
        } catch (SQLException sqle) {
            Util.getInstance().error(sqle.getMessage());
        } catch (IndexOutOfBoundsException ie) {
            Util.getInstance().error(ie.getMessage());
        } finally {
            close();
        }

        return value;
    }

    public int getLeastGuesses(int difficulty, int generation) {
        String query = "SELECT MIN(" + StatData.COLUMN_GUESSES + ") FROM " + StatData.TABLE_NAME;
        query += (generation != Globals.getInstance().GENERATION_NULL) ? getJoinClauseForPokemonStats() : "";
        if (difficulty != Globals.getInstance().DIFFICULTY_NULL) {
            query += (generation != Globals.getInstance().GENERATION_NULL) ? getWhereClauseForDifficulty(difficulty) + getWhereClauseForPokemonGeneration(false, generation) : getWhereClauseForDifficulty(difficulty);
        } else {
            query += (generation != Globals.getInstance().GENERATION_NULL) ? getWhereClauseForPokemonGeneration(true, generation) : "";
        }
        query += getWhereClauseForWonGame(!query.contains("WHERE"));

        return getSingleColumnValueFromQuery(query);
    }

    public int getMostGuesses(int difficulty, int generation) {
        String query = "SELECT MAX(" + StatData.COLUMN_GUESSES + ") FROM " + StatData.TABLE_NAME;
        query += (generation != Globals.getInstance().GENERATION_NULL) ? getJoinClauseForPokemonStats() : "";
        if (difficulty != Globals.getInstance().DIFFICULTY_NULL) {
            query += (generation != Globals.getInstance().GENERATION_NULL) ? getWhereClauseForDifficulty(difficulty) + getWhereClauseForPokemonGeneration(false, generation) : getWhereClauseForDifficulty(difficulty);
        } else {
            query += (generation != Globals.getInstance().GENERATION_NULL) ? getWhereClauseForPokemonGeneration(true, generation) : "";
        }
        query += getWhereClauseForWonGame(!query.contains("WHERE"));

        return getSingleColumnValueFromQuery(query);
    }

    public int getGameCount(int difficulty, int generation) {
        String query = "SELECT COUNT(*) FROM " + StatData.TABLE_NAME;
        query += (generation != Globals.getInstance().GENERATION_NULL) ? getJoinClauseForPokemonStats() : "";
        if (difficulty != Globals.getInstance().DIFFICULTY_NULL) {
            query += (generation != Globals.getInstance().GENERATION_NULL) ? getWhereClauseForDifficulty(difficulty) + getWhereClauseForPokemonGeneration(false, generation) : getWhereClauseForDifficulty(difficulty);
        } else {
            query += (generation != Globals.getInstance().GENERATION_NULL) ? getWhereClauseForPokemonGeneration(true, generation) : "";
        }

        return getSingleColumnValueFromQuery(query);
    }

    public int getGuesses(int difficulty, int generation) {
        String query = "SELECT SUM(" + StatData.COLUMN_GUESSES + ") FROM " + StatData.TABLE_NAME;
        query += (generation != Globals.getInstance().GENERATION_NULL) ? getJoinClauseForPokemonStats() : "";
        if (difficulty != Globals.getInstance().DIFFICULTY_NULL) {
            query += (generation != Globals.getInstance().GENERATION_NULL) ? getWhereClauseForDifficulty(difficulty) + getWhereClauseForPokemonGeneration(false, generation) : getWhereClauseForDifficulty(difficulty);
        } else {
            query += (generation != Globals.getInstance().GENERATION_NULL) ? getWhereClauseForPokemonGeneration(true, generation) : "";
        }

        return getSingleColumnValueFromQuery(query);
    }

    public int getValidGuesses(int difficulty, int generation) {
        String query = "SELECT SUM(" + StatData.COLUMN_VALID_GUESSES + ") FROM " + StatData.TABLE_NAME;
        query += (generation != Globals.getInstance().GENERATION_NULL) ? getJoinClauseForPokemonStats() : "";
        if (difficulty != Globals.getInstance().DIFFICULTY_NULL) {
            query += (generation != Globals.getInstance().GENERATION_NULL) ? getWhereClauseForDifficulty(difficulty) + getWhereClauseForPokemonGeneration(false, generation) : getWhereClauseForDifficulty(difficulty);
        } else {
            query += (generation != Globals.getInstance().GENERATION_NULL) ? getWhereClauseForPokemonGeneration(true, generation) : "";
        }

        return getSingleColumnValueFromQuery(query);
    }

    public int getGamesWon(int difficulty, int generation) {
        String query = "SELECT SUM(" + StatData.COLUMN_WON_GAME + ") FROM " + StatData.TABLE_NAME;
        query += (generation != Globals.getInstance().GENERATION_NULL) ? getJoinClauseForPokemonStats() : "";
        if (difficulty != Globals.getInstance().DIFFICULTY_NULL) {
            query += (generation != Globals.getInstance().GENERATION_NULL) ? getWhereClauseForDifficulty(difficulty) + getWhereClauseForPokemonGeneration(false, generation) : getWhereClauseForDifficulty(difficulty);
        } else {
            query += (generation != Globals.getInstance().GENERATION_NULL) ? getWhereClauseForPokemonGeneration(true, generation) : "";
        }
        query += getWhereClauseForWonGame(!query.contains("WHERE"));

        return getSingleColumnValueFromQuery(query);
    }

    public int getGameTime(int difficulty, int generation) {
        String query = "SELECT SUM(" + StatData.COLUMN_GAME_TIME + ") FROM " + StatData.TABLE_NAME;
        query += (generation != Globals.getInstance().GENERATION_NULL) ? getJoinClauseForPokemonStats() : "";
        if (difficulty != Globals.getInstance().DIFFICULTY_NULL) {
            query += (generation != Globals.getInstance().GENERATION_NULL) ? getWhereClauseForDifficulty(difficulty) + getWhereClauseForPokemonGeneration(false, generation) : getWhereClauseForDifficulty(difficulty);
        } else {
            query += (generation != Globals.getInstance().GENERATION_NULL) ? getWhereClauseForPokemonGeneration(true, generation) : "";
        }

        return getSingleColumnValueFromQuery(query);
    }

    public int getGameCountForPokemon(int difficulty, int pokemonid) {
        return getSingleColumnValueFromQuery("SELECT COUNT(*) FROM " + StatData.TABLE_NAME +
                getWhereClauseForDifficulty(difficulty) +
                " AND " + StatData.COLUMN_POKEMON_ID + " = " + Util.getInstance().wrap(pokemonid));
    }

    public double getAverageGameTime(int difficulty, int generation) {
        String query = "SELECT AVG(" + StatData.COLUMN_GAME_TIME + ") FROM " + StatData.TABLE_NAME;
        query += (generation != Globals.getInstance().GENERATION_NULL) ? getJoinClauseForPokemonStats() : "";
        if (difficulty != Globals.getInstance().DIFFICULTY_NULL) {
            query += (generation != Globals.getInstance().GENERATION_NULL) ? getWhereClauseForDifficulty(difficulty) + getWhereClauseForPokemonGeneration(false, generation) : getWhereClauseForDifficulty(difficulty);
        } else {
            query += (generation != Globals.getInstance().GENERATION_NULL) ? getWhereClauseForPokemonGeneration(true, generation) : "";
        }

        return getSingleColumnDoubleFromQuery(query);
    }

    public String getMostPlayedPokemonString(int difficulty, int generation) {
        String query = "SELECT " + StatData.COLUMN_POKEMON_ID + ", COUNT(*) AS c FROM " + StatData.TABLE_NAME;
        query += (generation != Globals.getInstance().GENERATION_NULL) ? getJoinClauseForPokemonStats() : "";
        if (difficulty != Globals.getInstance().DIFFICULTY_NULL) {
            query += (generation != Globals.getInstance().GENERATION_NULL) ? getWhereClauseForDifficulty(difficulty) + getWhereClauseForPokemonGeneration(false, generation) : getWhereClauseForDifficulty(difficulty);
        } else {
            query += (generation != Globals.getInstance().GENERATION_NULL) ? getWhereClauseForPokemonGeneration(true, generation) : "";
        }
        query += " GROUP BY " + StatData.COLUMN_POKEMON_ID + " ORDER BY c DESC";

        int[] columns = getMultiIntColumnValueFromQuery(query);
        PokemonUtil pokemonUtil = new PokemonUtil();
        String pokeName = "";
        int gameCount = 0;
        if (columns != null) {
            pokeName = pokemonUtil.getPokemonBasedOnNumber(columns[0]).getName();
            gameCount = columns[1];
        }
        return Util.getInstance().capitalizeFirstLetter(pokeName) + " (x" + gameCount + ")";
    }

    public String getMostPlayedPokemonString() {
        int[] columns = getMultiIntColumnValueFromQuery("SELECT " + StatData.COLUMN_POKEMON_ID + ", COUNT(*) AS c FROM " + StatData.TABLE_NAME +
                " GROUP BY " + StatData.COLUMN_POKEMON_ID + " ORDER BY c DESC");
        PokemonUtil pokeUtil = new PokemonUtil();
        return Util.getInstance().capitalizeFirstLetter(pokeUtil.getPokemonBasedOnNumber(columns[0]).getName()) + " (x" + columns[2] + ")";
    }

    public int getMostPlayedPokemonId(int difficulty) {
        return getSingleColumnValueFromQuery("SELECT " + StatData.COLUMN_POKEMON_ID + ", COUNT(*) AS c FROM " + StatData.TABLE_NAME +
                getWhereClauseForDifficulty(difficulty) + " GROUP BY " + StatData.COLUMN_POKEMON_ID + " ORDER BY c DESC");
    }
}
