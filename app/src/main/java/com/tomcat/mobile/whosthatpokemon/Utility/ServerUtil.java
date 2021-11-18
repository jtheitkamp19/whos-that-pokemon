package com.tomcat.mobile.whosthatpokemon.Utility;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.tomcat.mobile.whosthatpokemon.Modules.Pokemon;
import com.tomcat.mobile.whosthatpokemon.Modules.Type;
import com.tomcat.mobile.whosthatpokemon.R;

import org.json.JSONArray;
import org.json.JSONException;

public class ServerUtil {
    final String BASE_FILE_URL = "https://pokemon-services.herokuapp.com/";
    final DataAccessHelper dah;
    private Response.ErrorListener errorListener;
    RequestQueue queue;
    Context appContext;
    boolean hasServerPullFailed = false;

    public ServerUtil(Context context) {
        appContext = context;
        queue = Volley.newRequestQueue(appContext);
        this.dah = DataAccessHelper.getInstance();

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.getInstance().setPropertyHasLocalData(false);
                hasServerPullFailed = true;
                Util.getInstance().error("" + error.getMessage().length());
                Util.getInstance().error("Response: " + error);
            }
        };
    }

    public void pullServerData(final Handler.Callback callback, final ProgressBar progressBar, final TextView text) {
        Util.getInstance().setPropertyHasLocalData(false);
        progressBar.setMax(3);
        final String currentProgress = appContext.getResources().getString(R.string.progress_bar);
        text.setText(currentProgress.replace(appContext.getResources().getString(R.string.current_progress), "0"));
        pullPokemonData(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                progressBar.setProgress(1);
                text.setText(currentProgress.replace(appContext.getResources().getString(R.string.current_progress), "1"));
                pullTypeData(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message message) {
                        progressBar.setProgress(3);
                        text.setText(currentProgress.replace(appContext.getResources().getString(R.string.current_progress), "7"));
                        Util.getInstance().setPropertyHasLocalData(true);
                        Message msg = new Message();
                        callback.handleMessage(msg);
                        return true;
                    }
                });
                return true;
            }
        });
    }

    public void pullTypeData(final Handler.Callback callback) {
        String url = BASE_FILE_URL + "api/v1/types";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                TypeUtil typeUtil = new TypeUtil();
                try {
                    Util.getInstance().log("Types found: " + response.length());
                    for (int i = 0; i < response.length(); i++) {
                        Type type = new Type(response.getJSONObject(i));
                        typeUtil.addType(type.getContentValuesForType());
                    }

                    Message msg = new Message();
                    callback.handleMessage(msg);
                } catch (JSONException jsone) {
                    Util.getInstance().error(jsone.getMessage());
                }
            }
        }, errorListener);

        queue.add(jsonArrayRequest);
    }

    public void pullPokemonData(final Handler.Callback callback) {
        String url = BASE_FILE_URL + "api/v1/pokemon";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                PokemonUtil pokemonUtil = new PokemonUtil();
                try {
                    Util.getInstance().log("Pokemon found: " + response.length());
                    for (int i = 0; i < response.length(); i++) {
                        Pokemon pokemon = new Pokemon(response.getJSONObject(i));
                        pokemonUtil.addPokemon(pokemon.getContentValuesForPokemon());
                    }

                    Message msg = new Message();
                    callback.handleMessage(msg);
                } catch (JSONException jsone) {
                    Util.getInstance().error(jsone.getMessage());
                }
            }
        }, errorListener);

        queue.add(jsonArrayRequest);
    }
}
