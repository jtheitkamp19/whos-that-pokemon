package com.tomcat.mobile.whosthatpokemon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tomcat.mobile.whosthatpokemon.Modules.Pokemon;
import com.tomcat.mobile.whosthatpokemon.Utility.Globals;
import com.tomcat.mobile.whosthatpokemon.Utility.PokemonUtil;
import com.tomcat.mobile.whosthatpokemon.Utility.Util;

public class DifficultySelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_selection);
    }

    public void onDifficultySelectionClickHandler(View view) {
        Button b = (Button)view;
        String text = (String)b.getText();
        Intent intent;

        if (text.equals(getString(R.string.easy))) {
            intent = new Intent(this, GenerationSelection.class);
            intent.putExtra(getString(R.string.arg_multiple_select), false);
        } else if (text.equals(getString(R.string.intermediate))) {
            intent = new Intent(this, GenerationSelection.class);
            intent.putExtra(getString(R.string.arg_multiple_select), true);
        } else {
            intent = new Intent(this, GameActivity.class);
            PokemonUtil pokemonUtil = new PokemonUtil();
            Pokemon p = pokemonUtil.getRandomPokemon();
            intent.putExtra(getString(R.string.arg_pokemon), p);

            int difficulty = (text.equals(getString(R.string.hard))) ? Globals.getInstance().DIFFICULTY_HARD : Globals.getInstance().DIFFICULTY_BRUTAL;
            intent.putExtra(getString(R.string.arg_difficulty), difficulty);
        }

        startActivity(intent);
    }
}