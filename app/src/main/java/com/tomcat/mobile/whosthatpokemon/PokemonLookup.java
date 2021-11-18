package com.tomcat.mobile.whosthatpokemon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tomcat.mobile.whosthatpokemon.Modules.Pokemon;
import com.tomcat.mobile.whosthatpokemon.Modules.PokemonTypes;
import com.tomcat.mobile.whosthatpokemon.Utility.PokemonTypesUtil;
import com.tomcat.mobile.whosthatpokemon.Utility.PokemonUtil;
import com.tomcat.mobile.whosthatpokemon.Utility.Util;

import java.util.ArrayList;
import java.util.List;

public class PokemonLookup extends AppCompatActivity {
    ListView familyView;
    ArrayList<String> familyArray = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    boolean haveClickListenersBeenAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_lookup);
        familyView = (ListView)findViewById(R.id.m_lvGuesses);
        arrayAdapter = new ArrayAdapter(this, R.layout.activity_listview, familyArray);
        familyView.setAdapter(arrayAdapter);
        ((ListView)familyView).addOnLayoutChangeListener(layoutChangeListener);

        displayPokemonData((Pokemon)getIntent().getSerializableExtra(getString(R.string.arg_pokemon)));
    }

    private void displayPokemonData(Pokemon pokemon) {
        PokemonTypesUtil pokemonTypesUtil = new PokemonTypesUtil();
        PokemonUtil pokeUtil = new PokemonUtil();
        PokemonTypes[] types = pokemonTypesUtil.getPokemonTypes(pokemon.getNumber());

        if (pokeUtil.isPokemonValid(pokemon) && types.length >= 1) {
            ((TextView)findViewById(R.id.m_tvName)).setText(Util.getInstance().capitalizeFirstLetter(pokemon.getName()));
            ((TextView)findViewById(R.id.m_tvNumber)).setText("" + pokemon.getNumber());
            ((TextView)findViewById(R.id.m_tvColor)).setText(Util.getInstance().capitalizeFirstLetter(pokemon.getColor()));
            ((TextView)findViewById(R.id.m_tvHeight)).setText("" + pokemon.getHeight() + "\"");
            ((TextView)findViewById(R.id.m_tvWeight)).setText("" + pokemon.getWeight() + " kg");
            ((TextView)findViewById(R.id.m_tvEvoNum)).setText("" + pokemon.getEvoNum());
            ((TextView)findViewById(R.id.m_tvFamilyMembers)).setText("" + pokeUtil.getFamilyMemberCountForPokemon(pokemon.getNumber()));
            ((TextView)findViewById(R.id.m_tvHp)).setText("" + pokemon.getHp());
            ((ProgressBar)findViewById(R.id.m_pbHp)).setProgress(pokemon.getHp());
            ((TextView)findViewById(R.id.m_tvAtk)).setText("" + pokemon.getAttack());
            ((ProgressBar)findViewById(R.id.m_pbAtk)).setProgress(pokemon.getAttack());
            ((TextView)findViewById(R.id.m_tvDef)).setText("" + pokemon.getDefense());
            ((ProgressBar)findViewById(R.id.m_pbDef)).setProgress(pokemon.getDefense());
            ((TextView)findViewById(R.id.m_tvSpAtk)).setText("" + pokemon.getSpAtk());
            ((ProgressBar)findViewById(R.id.m_pbSpAtk)).setProgress(pokemon.getSpAtk());
            ((TextView)findViewById(R.id.m_tvSpDef)).setText("" + pokemon.getSpDef());
            ((ProgressBar)findViewById(R.id.m_pbSpDef)).setProgress(pokemon.getSpDef());
            ((TextView)findViewById(R.id.m_tvSpeed)).setText("" + pokemon.getSpeed());
            ((ProgressBar)findViewById(R.id.m_pbSpeed)).setProgress(pokemon.getSpeed());

            if (types.length == 1) {
                ((TextView)findViewById(R.id.m_tvType1)).setText(Util.getInstance().capitalizeFirstLetter(types[0].getType()));
                ((TextView)findViewById(R.id.m_tvType2)).setVisibility(View.INVISIBLE);
            } else {
                ((TextView)findViewById(R.id.m_tvType1)).setText(Util.getInstance().capitalizeFirstLetter(types[0].getType()));
                ((TextView)findViewById(R.id.m_tvType2)).setText(Util.getInstance().capitalizeFirstLetter(types[1].getType()));
            }

            int imageId = Util.getInstance().getResourceIdFromName(pokemon.getName(), R.drawable.class);
            if (imageId != -1) {
                ((ImageView)findViewById(R.id.m_ivPokemon)).setImageResource(imageId);
            } else {
                Util.getInstance().error("Image could not be found for " + pokemon.getName());
            }

            Pokemon[] pokemons = pokeUtil.getPokemonFamilyMembersForPokemon(pokemon.getNumber());
            for (int i = 0; i < pokemons.length; i++) {
                familyArray.add(Util.getInstance().capitalizeFirstLetter(pokemons[i].getName()) + " - " + pokemons[i].getNumber());
            }
            arrayAdapter.notifyDataSetChanged();
        } else {
            Util.getInstance().error("Selected pokemon is invalid or does not have types associated with it");
            AlertDialog.Builder invalidAlert = Util.getInstance().createAlertDialogWithTitleAndMessage(this, "Invalid Pokemon", "Data for the selected Pokemon is invalid. Please try repulling all data or tell Jordan to fix this.");
            invalidAlert.show();
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PokemonUtil pokeUtil = new PokemonUtil();
            String text = String.valueOf(((TextView)view).getText());
            int number = Integer.parseInt(text.split(" - ")[1]);
            Pokemon p = pokeUtil.getPokemonBasedOnNumber(number);
            Intent intent = new Intent(getApplicationContext(), PokemonLookup.class);
            intent.putExtra(getString(R.string.arg_pokemon), p);
            startActivity(intent);
        }
    };

    View.OnLayoutChangeListener layoutChangeListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
            if (!haveClickListenersBeenAdded) {
                Util.getInstance().log("Setting click listeners");
                for (int x = 0; x < familyView.getChildCount(); x++) {
                    TextView pokeView = (TextView)familyView.getChildAt(x);
                    pokeView.setOnClickListener(listener);
                }
                haveClickListenersBeenAdded = true;
            } else {
                Util.getInstance().log("Not adding click listeners because they have already been enabled.");
            }
        }
    };
}