package com.tomcat.mobile.whosthatpokemon.Modules;

import android.content.ContentValues;

import com.tomcat.mobile.whosthatpokemon.DataTables.StatData;

public class Stats {
    private int id = 0;
    private int pokemonid = 0;
    private int guesses = 0;
    private int validguesses = 0;
    private int gametime = 0;
    private int difficulty = 0;
    private int wongame = 0;

    public Stats() {

    }

    public int getId() {
        return id;
    }

    public int getPokemonId() {
        return pokemonid;
    }

    public int getGuesses() {
        return guesses;
    }

    public int getValidGuesses() {
        return validguesses;
    }

    public int getGameTime() {
        return gametime;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getWonGame() {
        return wongame;
    }

    public void setId(int l) {
        id = l;
    }

    public void setPokemonId(int i) {
        pokemonid = i;
    }

    public void setGuesses(int i) {
        guesses = i;
    }

    public void setValidGuesses(int i) {
        validguesses = i;
    }

    public void setGameTime(int i) {
        gametime = i;
    }

    public void setDifficulty(int i) {
        difficulty = i;
    }

    public void setWonGame(int i) {
        wongame = i;
    }

    public ContentValues getContentValuesForStat() {
        ContentValues cv = new ContentValues();

        cv.put(StatData.COLUMN_POKEMON_ID, pokemonid);
        cv.put(StatData.COLUMN_GUESSES, guesses);
        cv.put(StatData.COLUMN_VALID_GUESSES, validguesses);
        cv.put(StatData.COLUMN_GAME_TIME, gametime);
        cv.put(StatData.COLUMN_DIFFICULTY, difficulty);
        cv.put(StatData.COLUMN_WON_GAME, wongame);

        return cv;
    }
}
