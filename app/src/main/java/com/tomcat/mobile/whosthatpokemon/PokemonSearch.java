package com.tomcat.mobile.whosthatpokemon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tomcat.mobile.whosthatpokemon.Modules.Pokemon;
import com.tomcat.mobile.whosthatpokemon.Utility.PokemonUtil;
import com.tomcat.mobile.whosthatpokemon.Utility.Util;

public class PokemonSearch extends AppCompatActivity {
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_search);

        ((EditText)findViewById(R.id.m_etSearch)).addTextChangedListener(watcher);
        layout = findViewById(R.id.m_llSearch);
    }

    private void addPokemonToSearch(Pokemon[] pokemons) {
        layout.removeAllViews();
        for (int x = 0; x < pokemons.length; x++) {
            TextView view = new TextView(this);
            view.setTextAppearance(R.style.DefaultText);
            view.setText(pokemons[x].getName() + " - " + pokemons[x].getNumber());
            view.setOnClickListener(listener);
            layout.addView(view);
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PokemonUtil pokeUtil = new PokemonUtil();
            String text = String.valueOf(((TextView)view).getText());
            int number = Integer.parseInt(text.split(" - ")[1]);
            Pokemon p = pokeUtil.getPokemonBasedOnNumber(number);
            Util.getInstance().error(p.toString());
            Intent intent = new Intent(getApplicationContext(), PokemonLookup.class);
            intent.putExtra(getString(R.string.arg_pokemon), p);
            startActivity(intent);
        }
    };

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String text = String.valueOf(charSequence);
            text = Util.getInstance().formatStringForDatabase(text);
            PokemonUtil pokeUtil = new PokemonUtil();

            Pokemon[] pokemons = pokeUtil.getTopNPokemonFromNameSearch(10, text);
            addPokemonToSearch(pokemons);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}