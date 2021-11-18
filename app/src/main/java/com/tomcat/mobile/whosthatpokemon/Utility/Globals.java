package com.tomcat.mobile.whosthatpokemon.Utility;

import java.util.regex.Pattern;

public class Globals {
    private static Globals globals = null;

    public final int DIFFICULTY_NULL = -1;
    public final int DIFFICULTY_EASY = 0;
    public final int DIFFICULTY_INTERMEDIATE = 1;
    public final int DIFFICULTY_HARD = 2;
    public final int DIFFICULTY_BRUTAL = 3;
    public final int WON_GAME = 1;
    public final int LOST_GAME = 0;
    public final int NUMBER_OF_GENERATIONS = 8;
    public final int GENERATION_NULL = -1;

    public final Pattern REGEX_NUMBER = Pattern.compile("^(\\d*)(.{0,1})(\\d+)$");

    private Globals() {

    }

    public static void setInstance() {
        globals = new Globals();
    }

    public static synchronized Globals getInstance() {
        return globals;
    }
}
