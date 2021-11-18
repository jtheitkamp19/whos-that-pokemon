package com.tomcat.mobile.whosthatpokemon;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tomcat.mobile.whosthatpokemon.DataTables.StatData;
import com.tomcat.mobile.whosthatpokemon.Modules.Pokemon;
import com.tomcat.mobile.whosthatpokemon.Modules.Type;
import com.tomcat.mobile.whosthatpokemon.Utility.Globals;
import com.tomcat.mobile.whosthatpokemon.Utility.PokemonUtil;
import com.tomcat.mobile.whosthatpokemon.Utility.StatUtil;
import com.tomcat.mobile.whosthatpokemon.Utility.TypeUtil;
import com.tomcat.mobile.whosthatpokemon.Utility.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    final int ASCII_OFFSET = 48;
    final double HEIGHT_DIFFERENTIAL_MAXIMUM = .5;
    final double WEIGHT_DIFFERENTIAL_MAXIMUM = 5.0;
    private int guesses = 0;
    private int validGuesses = 0;
    private int secondsRemaining = Util.GAME_TIME;
    EditText m_etName;
    ImageView pokeView;
    Handler timeHandler;
    ListView guessesView;
    ArrayList<String> guessesArray = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    Timer timer;
    Pokemon pokemon;
    int[] eligibleGenerations;
    int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        pokeView = findViewById(R.id.m_ivPokemon);
        pokeView.setImageResource(R.drawable.ultraball);
        guessesView = findViewById(R.id.m_lvGuesses);
        ((TextView)findViewById(R.id.m_tvTime)).setText(getString(R.string.time_remaining) + " 05:00");
        m_etName = (EditText)findViewById(R.id.m_etName);
        pokemon = (Pokemon)getIntent().getSerializableExtra(getString(R.string.arg_pokemon));

        difficulty = getIntent().getIntExtra(getString(R.string.arg_difficulty), Globals.getInstance().DIFFICULTY_BRUTAL);
        TextView generationsTextView = findViewById(R.id.m_tvGenerations);
        if (difficulty == Globals.getInstance().DIFFICULTY_EASY) {
            eligibleGenerations = new int[1];
            eligibleGenerations[0] = pokemon.getGeneration();
            generationsTextView.setText(getString(R.string.generations) + pokemon.getGeneration());
        } else if (difficulty == Globals.getInstance().DIFFICULTY_INTERMEDIATE) {
            eligibleGenerations = new int[4];
            eligibleGenerations[0] = getIntent().getIntExtra(getString(R.string.arg_generation1), 0);
            eligibleGenerations[1] = getIntent().getIntExtra(getString(R.string.arg_generation2), 0);
            eligibleGenerations[2] = getIntent().getIntExtra(getString(R.string.arg_generation3), 0);
            eligibleGenerations[3] = getIntent().getIntExtra(getString(R.string.arg_generation4), 0);
            generationsTextView.setText(getString(R.string.generations) + " " + eligibleGenerations[0] + ", " + eligibleGenerations[1] + ", " + eligibleGenerations[2] + ", " + eligibleGenerations[3]);
        } else if (difficulty == Globals.getInstance().DIFFICULTY_HARD) {
            boolean containsPokemonGeneration = false;
            eligibleGenerations = new int[4];
            for (int i = 0; i < eligibleGenerations.length; i++) {
                int random = (int)(Math.random() * Globals.getInstance().NUMBER_OF_GENERATIONS) + 1;
                boolean isnew = true;
                for (int x = 0; x < eligibleGenerations.length; x++) {
                    if (eligibleGenerations[x] == random) {
                        i--;
                        isnew = false;
                        break;
                    }
                }

                if (isnew) {
                    containsPokemonGeneration = random == pokemon.getGeneration() || containsPokemonGeneration;
                    eligibleGenerations[i] = random;
                }
            }

            if (!containsPokemonGeneration) {
                eligibleGenerations[(int)(Math.random() * eligibleGenerations.length)] = pokemon.getGeneration();
            }

            generationsTextView.setText(getString(R.string.generations) + " " + eligibleGenerations[0] + ", " + eligibleGenerations[1] + ", " + eligibleGenerations[2] + ", " + eligibleGenerations[3]);
        } else {
            eligibleGenerations = new int[Globals.getInstance().NUMBER_OF_GENERATIONS];
            for (int i = 0; i < eligibleGenerations.length; i++) {
                eligibleGenerations[i] = i + 1;
            }
            generationsTextView.setVisibility(View.INVISIBLE);
        }

        ((EditText)findViewById(R.id.m_etName)).addTextChangedListener(textListener);
        arrayAdapter = new ArrayAdapter(this, R.layout.activity_listview, guessesArray);
        guessesView.setAdapter(arrayAdapter);
        ((ListView)guessesView).addOnLayoutChangeListener(layoutChangeListener);

        timeHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                secondsRemaining--;
                String time = Util.getInstance().secondsToTime(secondsRemaining);
                ((TextView)findViewById(R.id.m_tvTime)).setText(getString(R.string.time_remaining) + " " + time);

                if (secondsRemaining <= 0) {
                    secondsRemaining = 0;
                    ((TextView)findViewById(R.id.m_tvTime)).setText(getString(R.string.time_remaining) + "00:00");
                    timer.cancel();
                    loseGame();
                }
            }
        };

        timer = new Timer();
        timer.scheduleAtFixedRate(timeTick, 0, Util.ONE_SECOND_IN_MILLISECONDS);
    }

    private void saveStatsToDatabase(int wongame) {
        StatUtil statUtil = new StatUtil();
        ContentValues cv = new ContentValues();

        cv.put(StatData.COLUMN_POKEMON_ID, pokemon.getNumber());
        cv.put(StatData.COLUMN_GUESSES, guesses);
        cv.put(StatData.COLUMN_VALID_GUESSES, validGuesses);
        cv.put(StatData.COLUMN_DIFFICULTY, difficulty);
        cv.put(StatData.COLUMN_WON_GAME, wongame);
        cv.put(StatData.COLUMN_GAME_TIME, Util.GAME_TIME - secondsRemaining);

        statUtil.addGameStats(cv);
    }

    private void endGame(int wongame) {
        timer.cancel();
        ((EditText)findViewById(R.id.m_etName)).removeTextChangedListener(textListener);
        guessesView.removeOnLayoutChangeListener(layoutChangeListener);
        saveStatsToDatabase(wongame);

        PokemonUtil pokeUtil = new PokemonUtil();
        TypeUtil typeUtil = new TypeUtil();
        List<Type> types = typeUtil.getTypesFromString(pokemon.getTypes());

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

        if (types.size() == 1) {
            ((TextView)findViewById(R.id.m_tvType1)).setText(Util.getInstance().capitalizeFirstLetter(types.get(0).getType()));
            ((TextView)findViewById(R.id.m_tvType2)).setVisibility(View.INVISIBLE);
        } else {
            ((TextView)findViewById(R.id.m_tvType1)).setText(Util.getInstance().capitalizeFirstLetter(types.get(0).getType()));
            ((TextView)findViewById(R.id.m_tvType2)).setText(Util.getInstance().capitalizeFirstLetter(types.get(1).getType()));
        }

        int imageId = Util.getInstance().getResourceIdFromName(pokemon.getName(), R.drawable.class);
        if (imageId != -1) {
            pokeView.setImageResource(imageId);
        } else {
            Util.getInstance().error("Image could not be found for " + pokemon.getName());
        }

        findViewById(R.id.m_tvName).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.m_tvName)).setText(Util.getInstance().capitalizeFirstLetter(pokemon.getName()));
        findViewById(R.id.m_etName).setVisibility(View.INVISIBLE);
        findViewById(R.id.m_etName).setFocusable(false);
        findViewById(R.id.m_etName).setEnabled(false);

        Util.getInstance().closeKeyboard(this);
    }

    private void loseGame() {
        endGame(Globals.getInstance().LOST_GAME);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("Sorry, you lost the game")
                .setTitle("Oh no!");
        builder.show();
    }

    private void winGame() {
        endGame(Globals.getInstance().WON_GAME);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("You won the game!")
                .setTitle("Congratulations!");
        builder.show();
    }

    public void onPlayGameSameSettingsClickHandler(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        PokemonUtil pokemonUtil = new PokemonUtil();
        Pokemon p;

        if (difficulty == Globals.getInstance().DIFFICULTY_EASY) {
            p = pokemonUtil.getRandomPokemonFromGeneration(pokemon.getGeneration());
        } else if (difficulty == Globals.getInstance().DIFFICULTY_INTERMEDIATE) {
            int generation = eligibleGenerations[(int)(Math.random() * 4)];
            p = pokemonUtil.getRandomPokemonFromGeneration(generation);
            intent.putExtra(getString(R.string.arg_generation1), eligibleGenerations[0]);
            intent.putExtra(getString(R.string.arg_generation2), eligibleGenerations[1]);
            intent.putExtra(getString(R.string.arg_generation3), eligibleGenerations[2]);
            intent.putExtra(getString(R.string.arg_generation4), eligibleGenerations[3]);
        } else {
            p = pokemonUtil.getRandomPokemon();
        }

        intent.putExtra(getString(R.string.arg_pokemon), p);
        intent.putExtra(getString(R.string.arg_difficulty), difficulty);
        startActivity(intent);
    }

    public void onPlayGameDifferentSettingsClickHandler(View view) {
        Intent intent = new Intent(this, DifficultySelection.class);
        startActivity(intent);
    }

    public void onReturnHomeClickHandler(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private boolean isGuessValid(int generation) {
        for (int i = 0; i < eligibleGenerations.length; i++) {
            if (eligibleGenerations[i] == generation) {
                return true;
            }
        }

        return false;
    }

    private void findEquivalenciesWithCurrentPokemon(Pokemon p) {
        PokemonUtil pokeUtil = new PokemonUtil();
        TypeUtil typeUtil = new TypeUtil();
        guesses++;
        validGuesses = (isGuessValid(p.getGeneration())) ? validGuesses + 1 : validGuesses;
        guessesArray.add(Util.getInstance().capitalizeFirstLetter(p.getName()));
        arrayAdapter.notifyDataSetChanged();
        List<Type> currentTypes = typeUtil.getTypesFromString(pokemon.getTypes());
        List<Type> newTypes = typeUtil.getTypesFromString(p.getTypes());

        if (p.getName().toLowerCase().equals(pokemon.getName().toLowerCase())) {
            winGame();
        } else {
            if (currentTypes.size() != 0) {
                for (int i = 0; i < newTypes.size(); i++) {
                    TextView[] type = {(TextView)findViewById(R.id.m_tvType1), (TextView)findViewById(R.id.m_tvType2)};
                    for (int x = 0; x < newTypes.size(); x++) {
                        if (currentTypes.get(i).getType().equals(newTypes.get(x).getType())) {
                            type[i].setText(Util.getInstance().capitalizeFirstLetter(currentTypes.get(i).getType()));
                        }
                    }
                }
            } else {
                TextView[] type = {(TextView)findViewById(R.id.m_tvType1), (TextView)findViewById(R.id.m_tvType2)};
                type[0].setText("Type data not found");
                type[1].setVisibility(View.INVISIBLE);
            }


            if (Math.abs(p.getHeight() - pokemon.getHeight()) < HEIGHT_DIFFERENTIAL_MAXIMUM) {
                ((TextView)findViewById(R.id.m_tvHeight)).setText("" + pokemon.getHeight() + "\"");
            }

            if (Math.abs(p.getWeight() - pokemon.getWeight()) < WEIGHT_DIFFERENTIAL_MAXIMUM) {
                ((TextView)findViewById(R.id.m_tvWeight)).setText("" + pokemon.getWeight() + " kg");
            }

            if (p.getColor().equals(pokemon.getColor())) {
                ((TextView)findViewById(R.id.m_tvColor)).setText(Util.getInstance().capitalizeFirstLetter(pokemon.getColor()));
            }

            if (p.getEvoNum() == pokemon.getEvoNum()) {
                ((TextView)findViewById(R.id.m_tvEvoNum)).setText("" + pokemon.getEvoNum());
            }

            if (pokeUtil.getFamilyMemberCountForPokemon(p.getFamilyId()) == pokeUtil.getFamilyMemberCountForPokemon(pokemon.getFamilyId())) {
                ((TextView)findViewById(R.id.m_tvFamilyMembers)).setText("" + pokeUtil.getFamilyMemberCountForPokemon(p.getFamilyId()));
            }

            if (p.getHp() == pokemon.getHp()) {
                ((TextView)findViewById(R.id.m_tvHp)).setText("" + p.getHp());
                ((ProgressBar)findViewById(R.id.m_pbHp)).setProgress(pokemon.getHp());
            }

            if (p.getAttack() == pokemon.getAttack()) {
                ((TextView)findViewById(R.id.m_tvAtk)).setText("" + p.getAttack());
                ((ProgressBar)findViewById(R.id.m_pbAtk)).setProgress(pokemon.getAttack());
            }

            if (p.getDefense() == pokemon.getDefense()) {
                ((TextView)findViewById(R.id.m_tvDef)).setText("" + p.getDefense());
                ((ProgressBar)findViewById(R.id.m_pbDef)).setProgress(pokemon.getDefense());
            }

            if (p.getSpAtk() == pokemon.getSpAtk()) {
                ((TextView)findViewById(R.id.m_tvSpAtk)).setText("" + p.getSpAtk());
                ((ProgressBar)findViewById(R.id.m_pbSpAtk)).setProgress(pokemon.getSpAtk());
            }

            if (p.getSpDef() == pokemon.getSpDef()) {
                ((TextView)findViewById(R.id.m_tvSpDef)).setText("" + p.getSpDef());
                ((ProgressBar)findViewById(R.id.m_pbSpDef)).setProgress(pokemon.getSpDef());
            }

            if (p.getSpeed() == pokemon.getSpeed()) {
                ((TextView)findViewById(R.id.m_tvSpeed)).setText("" + p.getSpeed());
                ((ProgressBar)findViewById(R.id.m_pbSpeed)).setProgress(pokemon.getSpeed());
            }
        }
    }

    View.OnLayoutChangeListener layoutChangeListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
            for (int x = 0; x < guessesView.getChildCount(); x++) {
                TextView pokeView = (TextView)guessesView.getChildAt(x);
                String text = String.valueOf(pokeView.getText()).toLowerCase();
                PokemonUtil pokeUtil = new PokemonUtil();
                ArrayList<Pokemon> p = pokeUtil.getPokemonBasedOnName(text);

                if (p.get(0).getFamilyId() == pokemon.getFamilyId()) {
                    pokeView.setTextColor(getColor(R.color.crystal));
                } else {
                    pokeView.setTextColor(getColor(R.color.silver));
                }
            }
        }
    };

    TimerTask timeTick = new TimerTask() {
        @Override
        public void run() {
            timeHandler.sendEmptyMessage(0);
        }
    };

    TextWatcher textListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String guess = String.valueOf(charSequence).toLowerCase();
            guess = Util.getInstance().formatStringForDatabase(guess);
            boolean hasPokemonBeenGuessed = false;
            ArrayList<Pokemon> pokemons = new ArrayList<>();

            PokemonUtil pokemonUtil = new PokemonUtil();

            for (int v = 0; v < guessesView.getChildCount(); v++) {
                if (String.valueOf(((TextView)guessesView.getChildAt(v)).getText()).toLowerCase().equals(guess.toLowerCase())) {
                    hasPokemonBeenGuessed = true;
                }
            }

            if (!hasPokemonBeenGuessed) {
                pokemons = pokemonUtil.getPokemonBasedOnName(guess);

                if (pokemons.size() >= 1) {
                    for (int p = 0; p < pokemons.size(); p++) {
                        findEquivalenciesWithCurrentPokemon(pokemons.get(p));
                    }
                    ((EditText)findViewById(R.id.m_etName)).setText("");
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}