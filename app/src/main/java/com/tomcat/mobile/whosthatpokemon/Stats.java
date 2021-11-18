package com.tomcat.mobile.whosthatpokemon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.tomcat.mobile.whosthatpokemon.Utility.Globals;
import com.tomcat.mobile.whosthatpokemon.Utility.PokemonUtil;
import com.tomcat.mobile.whosthatpokemon.Utility.StatUtil;
import com.tomcat.mobile.whosthatpokemon.Utility.Util;

public class Stats extends AppCompatActivity {
    TabLayout tabs;
    TabLayout generations;
    private int currentDifficulty = Globals.getInstance().DIFFICULTY_NULL;
    private int currentGeneration = Globals.getInstance().GENERATION_NULL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        tabs = findViewById(R.id.m_tlStats);
        tabs.addOnTabSelectedListener(tabSelectedListener);
        generations = findViewById(R.id.m_tlGeneration);
        generations.addOnTabSelectedListener(generationSelectedListener);
        Util.getInstance().log("Updating tab data to easy difficulty");
        updateTabData();
    }

    private void updateTabData() {
        StatUtil statUtil = new StatUtil();
        int gamesPlayed = statUtil.getGameCount(currentDifficulty, currentGeneration);
        int gamesWon = statUtil.getGamesWon(currentDifficulty, currentGeneration);
        double winPercentage = ((double)gamesWon / (double)gamesPlayed) * 100;
        String winPercentageString = String.format("%.2f", winPercentage) + "%";
        double averageTime = statUtil.getAverageGameTime(currentDifficulty, currentGeneration);

        ((TextView)findViewById(R.id.m_tvLeastGuesses)).setText("" + statUtil.getLeastGuesses(currentDifficulty, currentGeneration));
        ((TextView)findViewById(R.id.m_tvMostGuesses)).setText("" + statUtil.getMostGuesses(currentDifficulty, currentGeneration));
        ((TextView)findViewById(R.id.m_tvValidGuesses)).setText("" + statUtil.getValidGuesses(currentDifficulty, currentGeneration));
        ((TextView)findViewById(R.id.m_tvTotalGuesses)).setText("" + statUtil.getGuesses(currentDifficulty, currentGeneration));
        ((TextView)findViewById(R.id.m_tvGamesWon)).setText("" + gamesWon);
        ((TextView)findViewById(R.id.m_tvGamesPlayed)).setText("" + gamesPlayed);
        ((TextView)findViewById(R.id.m_tvTimeInGame)).setText(Util.getInstance().secondsToTime(statUtil.getGameTime(currentDifficulty, currentGeneration)));
        ((TextView)findViewById(R.id.m_tvMostPlayed)).setText(statUtil.getMostPlayedPokemonString(currentDifficulty, currentGeneration));
        ((TextView)findViewById(R.id.m_tvAverageTime)).setText(Util.getInstance().secondsToTime((int)averageTime));
        ((TextView)findViewById(R.id.m_tvWinPercentage)).setText(winPercentageString);
    }

    TabLayout.BaseOnTabSelectedListener generationSelectedListener = new TabLayout.BaseOnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            String text = (String)tab.getText();
            if (text.equals(getString(R.string.all))) {
                currentGeneration = Globals.getInstance().GENERATION_NULL;
            } else {
                currentGeneration = Integer.valueOf(text.substring(text.length() - 1));
            }

            Util.getInstance().log("Updating tab data with difficulty: " + currentDifficulty + ", and generation: " + currentGeneration);
            updateTabData();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    TabLayout.BaseOnTabSelectedListener tabSelectedListener = new TabLayout.BaseOnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if (tab.getText().equals(getString(R.string.easy))) {
                currentDifficulty = Globals.getInstance().DIFFICULTY_EASY;
            } else if (tab.getText().equals(getString(R.string.intermediate))) {
                currentDifficulty = Globals.getInstance().DIFFICULTY_INTERMEDIATE;
            } else if (tab.getText().equals(getString(R.string.hard))) {
                currentDifficulty = Globals.getInstance().DIFFICULTY_HARD;
            } else if (tab.getText().equals(getString(R.string.brutal))) {
                currentDifficulty = Globals.getInstance().DIFFICULTY_BRUTAL;
            } else if (tab.getText().equals(getString(R.string.all))) {
                currentDifficulty = Globals.getInstance().DIFFICULTY_NULL;
            }

            Util.getInstance().log("Updating tab data with difficulty: " + currentDifficulty + ", and generation: " + currentGeneration);
            updateTabData();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };
}