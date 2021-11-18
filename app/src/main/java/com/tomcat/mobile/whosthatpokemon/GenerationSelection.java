package com.tomcat.mobile.whosthatpokemon;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tomcat.mobile.whosthatpokemon.Modules.Pokemon;
import com.tomcat.mobile.whosthatpokemon.Utility.Globals;
import com.tomcat.mobile.whosthatpokemon.Utility.PokemonUtil;
import com.tomcat.mobile.whosthatpokemon.Utility.Util;

public class GenerationSelection extends AppCompatActivity {
    final int ASCII_OFFSET = 48;
    boolean isMultipleSelect;
    Button[] selectedButtons = {null, null, null, null};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generation_selection);
        isMultipleSelect = getIntent().getBooleanExtra(getString(R.string.arg_multiple_select), false);

        if (!isMultipleSelect) {
            findViewById(R.id.m_btnContinue).setVisibility(View.INVISIBLE);
        }
    }

    public void onGenerationSelectClickHandler(View view) {
        Button button = (Button)view;
        if (isMultipleSelect) {
            boolean wasSelected = false;
            //Check if the button had previously been selected
            for (int i = 0; i < selectedButtons.length; i++) {
                if (selectedButtons[i] != null) {
                    if (button.getText() == selectedButtons[i].getText()) {
                        selectedButtons[i].setBackgroundColor(getColor(R.color.blue));
                        selectedButtons[i] = null;
                        wasSelected = true;
                    }
                }
            }

            //Mark the button as selected
            for(int i = 0; i < selectedButtons.length; i++) {
                if (selectedButtons[i] == null && !wasSelected) {
                    selectedButtons[i] = button;
                    selectedButtons[i].setBackgroundColor(getColor(R.color.crystal));
                    break;
                }
            }
        } else {
            String str = (String)button.getText();
            int gen = Integer.valueOf(str.charAt(str.length() - 1)) - ASCII_OFFSET;
            Util.getInstance().log("" + gen);
            PokemonUtil pokemonUtil = new PokemonUtil();
            Pokemon pokemon = pokemonUtil.getRandomPokemonFromGeneration(gen);
            Util.getInstance().log(pokemon.getName());
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra(getString(R.string.arg_pokemon), pokemon);
            intent.putExtra(getString(R.string.arg_difficulty), Globals.getInstance().DIFFICULTY_EASY);
            startActivity(intent);
        }
    }

    public void onBackButtonClickHandler(View view) {
        Intent intent = new Intent(this, DifficultySelection.class);
        startActivity(intent);
    }

    public void onContinueButtonClickHandler(View view) {
        boolean isArrayFull = true;
        for (int i = 0; i < selectedButtons.length; i++) {
            if (selectedButtons[i] == null) {
                isArrayFull = false;
            }
        }

        if (!isArrayFull) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage("Please select 4 generations.")
                    .setTitle("Not enough generations");
            builder.show();
        } else {
            int[] potentialGenerations = new int[4];
            for (int i = 0; i < potentialGenerations.length; i++) {
                potentialGenerations[i] = Integer.valueOf(selectedButtons[i].getText().charAt(selectedButtons[i].getText().length() - 1)) - ASCII_OFFSET;
            }

            int generation = potentialGenerations[(int)(Math.random() * 4)];
            Util.getInstance().log("" + generation);
            PokemonUtil pokemonUtil = new PokemonUtil();
            Pokemon pokemon = pokemonUtil.getRandomPokemonFromGeneration(generation);
            Util.getInstance().log("Starting game with pokemon: " + pokemon.getName());
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra(getString(R.string.arg_pokemon), pokemon);
            intent.putExtra(getString(R.string.arg_generation1), potentialGenerations[0]);
            intent.putExtra(getString(R.string.arg_generation2), potentialGenerations[1]);
            intent.putExtra(getString(R.string.arg_generation3), potentialGenerations[2]);
            intent.putExtra(getString(R.string.arg_generation4), potentialGenerations[3]);
            intent.putExtra(getString(R.string.arg_difficulty), Globals.getInstance().DIFFICULTY_INTERMEDIATE);
            startActivity(intent);
        }
    }
}