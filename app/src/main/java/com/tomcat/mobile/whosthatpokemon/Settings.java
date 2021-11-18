package com.tomcat.mobile.whosthatpokemon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tomcat.mobile.whosthatpokemon.Utility.DataAccessHelper;
import com.tomcat.mobile.whosthatpokemon.Utility.ServerUtil;
import com.tomcat.mobile.whosthatpokemon.Utility.Util;

import org.w3c.dom.Text;

public class Settings extends AppCompatActivity {
    ConstraintLayout overlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        overlay = findViewById(R.id.m_clOverlay);
        overlay.setVisibility(View.INVISIBLE);
    }

    public void onResetStatsClickHandler(View view) {
        AlertDialog.Builder builder = Util.getInstance().createAlertDialogWithTitleAndMessage(this, getString(R.string.clear_stats), getString(R.string.clear_stats_message));
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DataAccessHelper.getInstance().resetStats(DataAccessHelper.getInstance().getWritableDatabase());
                Util.getInstance().log("Stats Cleared");
            }
        });
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }

    public void onClearDataClickHandler(View view) {
        AlertDialog.Builder builder = Util.getInstance().createAlertDialogWithTitleAndMessage(this, getString(R.string.clear_data), getString(R.string.clear_data_message));
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DataAccessHelper.getInstance().resetLocalData(DataAccessHelper.getInstance().getWritableDatabase());
                Util.getInstance().log("Data Cleared");
            }
        });
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }

    public void onVersionHistoryClickHandler(View view) {
        Intent intent = new Intent(this, VersionHistory.class);
        startActivity(intent);
    }

    public void onPullDataClickHandler(View view) {
        ServerUtil server = new ServerUtil(this);
        if (!Util.getInstance().getPropertyHasLocalData()) {
            overlay.setVisibility(View.VISIBLE);
            DataAccessHelper.getInstance().resetLocalData(DataAccessHelper.getInstance().getWritableDatabase());

            DataAccessHelper.getInstance().onCreate(DataAccessHelper.getInstance().getWritableDatabase());

            server.pullServerData(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message message) {
                    onServerResponseListener();
                    Util.getInstance().setPropertyHasLocalData(true);
                    return true;
                }
            }, (ProgressBar)findViewById(R.id.m_pbProgress), (TextView)findViewById(R.id.m_tvProgress));
        } else {
            Util.getInstance().log("Application already has local data, not pulling new data.");
            onServerResponseListener();
        }
    }

    private void onServerResponseListener() {
        overlay.setVisibility(View.GONE);
    }
}