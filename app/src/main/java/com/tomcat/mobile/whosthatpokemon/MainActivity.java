package com.tomcat.mobile.whosthatpokemon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tomcat.mobile.whosthatpokemon.Modules.Pokemon;
import com.tomcat.mobile.whosthatpokemon.Utility.DataAccessHelper;
import com.tomcat.mobile.whosthatpokemon.Utility.Globals;
import com.tomcat.mobile.whosthatpokemon.Utility.PokemonUtil;
import com.tomcat.mobile.whosthatpokemon.Utility.ServerUtil;
import com.tomcat.mobile.whosthatpokemon.Utility.Util;

public class MainActivity extends AppCompatActivity {

    public ServerUtil server;
    ProgressBar bar;
    ConstraintLayout overlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overlay = findViewById(R.id.m_clOverlay);
        overlay.bringToFront();
        bar = findViewById(R.id.m_pbProgress);
        Util.setInstance(this);
        Globals.setInstance();
        DataAccessHelper.setInstance(this);
        ((TextView)findViewById(R.id.m_tvVersion)).setText(getString(R.string.version) + " " + BuildConfig.VERSION_NAME);

        server = new ServerUtil(this);
        if (!Util.getInstance().getPropertyHasLocalData()) {
            overlay.setVisibility(View.VISIBLE);
            server.pullServerData(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message message) {
                    onServerResponseListener();
                    Util.getInstance().setPropertyHasLocalData(true);
                    return true;
                }
            }, bar, (TextView)findViewById(R.id.m_tvProgress));
        } else {
            onServerResponseListener();
        }
    }

    public void onPokemonLookupClickHandler(View view) {
        Intent lookupIntent = new Intent(this, PokemonSearch.class);
        startActivity(lookupIntent);
    }

    public void onStatsClickHandler(View view) {
        Intent statsIntent = new Intent(this, Stats.class);
        startActivity(statsIntent);
    }

    public void onSettingsClickHandler(View view) {
        Intent settingIntent = new Intent(this, Settings.class);
        startActivity(settingIntent);
    }

    private void onServerResponseListener() {
        Util.getInstance().log("Data downloaded. Calling onServerResponseListener");
        overlay.setVisibility(View.GONE);
    }

    public void onPlayGameClickHandler(View view) {
        if (Util.getInstance().getPropertyHasLocalData()) {
            Intent difficultySelect = new Intent(this, DifficultySelection.class);
            startActivity(difficultySelect);
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this)
                    .setTitle("Data Error")
                    .setMessage("Please pull the data from the server before playing.");
            alert.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}