package com.tomcat.mobile.whosthatpokemon.Utility;

import android.app.Activity;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tomcat.mobile.whosthatpokemon.Modules.Move;
import com.tomcat.mobile.whosthatpokemon.Modules.Pokemon;
import com.tomcat.mobile.whosthatpokemon.Modules.PokemonAbility;
import com.tomcat.mobile.whosthatpokemon.Modules.PokemonFamily;
import com.tomcat.mobile.whosthatpokemon.Modules.PokemonMove;
import com.tomcat.mobile.whosthatpokemon.Modules.PokemonTypes;
import com.tomcat.mobile.whosthatpokemon.Modules.Type;
import com.tomcat.mobile.whosthatpokemon.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerUtil {
    final String BASE_FILE_URL = "https://jordanheitkamp19.github.io/Pokemon/";
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
        progressBar.setMax(7);
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
                        progressBar.setProgress(2);
                        text.setText(currentProgress.replace(appContext.getResources().getString(R.string.current_progress), "2"));
                        pullPokemonTypeData(new Handler.Callback() {
                            @Override
                            public boolean handleMessage(@NonNull Message message) {
                                progressBar.setProgress(3);
                                text.setText(currentProgress.replace(appContext.getResources().getString(R.string.current_progress), "3"));
                                pullPokemonFamilyData(new Handler.Callback() {
                                    @Override
                                    public boolean handleMessage(@NonNull Message message) {
                                        progressBar.setProgress(4);
                                        text.setText(currentProgress.replace(appContext.getResources().getString(R.string.current_progress), "4"));
                                        pullMoveData(new Handler.Callback() {
                                            @Override
                                            public boolean handleMessage(@NonNull Message message) {
                                                progressBar.setProgress(5);
                                                text.setText(currentProgress.replace(appContext.getResources().getString(R.string.current_progress), "5"));
                                                pullPokemonMoveData(new Handler.Callback() {
                                                    @Override
                                                    public boolean handleMessage(@NonNull Message message) {
                                                        progressBar.setProgress(6);
                                                        text.setText(currentProgress.replace(appContext.getResources().getString(R.string.current_progress), "6"));
                                                        pullPokemonAbilityData(new Handler.Callback() {
                                                            @Override
                                                            public boolean handleMessage(@NonNull Message message) {
                                                                progressBar.setProgress(7);
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
                                                return true;
                                            }
                                        });
                                        return true;
                                    }
                                });
                                return true;
                            }
                        });
                        return true;
                    }
                });
                return true;
            }
        });
    }

    public void pullPokemonAbilityData(final Handler.Callback callback) {
        String url = BASE_FILE_URL + "pokemonabilitiesdb.json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                PokemonAbilityUtil pokemonAbilityUtil = new PokemonAbilityUtil();
                int count = 0;
                try {
                    for (int i = 0; i < response.length(); i++) {
                        for (int x = 0; x < response.getJSONArray(i).length(); x++) {
                            PokemonAbility ability = new PokemonAbility(response.getJSONArray(i).getJSONObject(x));
                            pokemonAbilityUtil.addPokemonAbility(ability.getContentValuesForType());
                            count++;
                        }
                    }
                    Util.getInstance().log("Pokemon Abilities pulled with " + count + " records");

                    Message msg = new Message();
                    callback.handleMessage(msg);
                } catch (JSONException jsone) {
                    Util.getInstance().error(jsone.getMessage());
                }
            }
        }, errorListener);

        queue.add(jsonArrayRequest);
    }

    public void pullPokemonMoveData(final Handler.Callback callback) {
        String url = BASE_FILE_URL + "pokemovesdb.json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                PokemonMoveUtil pokemonMoveUtil = new PokemonMoveUtil();
                int count = 0;
                try {
                    for (int i = 0; i < response.length(); i++) {
                        for (int x = 0; x < response.getJSONArray(i).length(); x++) {
                            PokemonMove move = new PokemonMove(response.getJSONArray(i).getJSONObject(x));
                            pokemonMoveUtil.addPokemonFamily(move.getContentValuesForType());
                            count++;
                        }
                    }
                    Util.getInstance().log("Pokemon Moves pulled with " + count + " records");

                    Message msg = new Message();
                    callback.handleMessage(msg);
                } catch (JSONException jsone) {
                    Util.getInstance().error(jsone.getMessage());
                }
            }
        }, errorListener);

        queue.add(jsonArrayRequest);
    }

    public void pullMoveData(final Handler.Callback callback) {
        String url = BASE_FILE_URL + "movedb.json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                MoveUtil moveUtil = new MoveUtil();
                try {
                    Util.getInstance().log("Moves found: " + response.length());
                    for (int i = 0; i < response.length(); i++) {
                        Move move = new Move(response.getJSONObject(i));
                        moveUtil.addMove(move.getContentValuesForType());
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

    public void pullPokemonFamilyData(final Handler.Callback callback) {
        String url = BASE_FILE_URL + "pokefamiliesdb.json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                PokemonFamilyUtil pokemonFamilyUtil = new PokemonFamilyUtil();
                try {
                    Util.getInstance().log("Pokemon Families found: " + response.length());
                    for (int i = 0; i < response.length(); i++) {
                        PokemonFamily family = new PokemonFamily(response.getJSONObject(i));
                        pokemonFamilyUtil.addPokemonFamily(family.getContentValuesForType());
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

    public void pullPokemonTypeData(final Handler.Callback callback) {
        String url = BASE_FILE_URL + "pokemontypesdb.json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                PokemonTypesUtil typeUtil = new PokemonTypesUtil();
                int count = 0;
                try {
                    for (int i = 0; i < response.length(); i++) {
                        for (int x = 0; x < response.getJSONArray(i).length(); x++) {
                            PokemonTypes type = new PokemonTypes(response.getJSONArray(i).getJSONObject(x));
                            typeUtil.addPokemonType(type.getContentValuesForType());
                            count++;
                        }
                    }
                    Util.getInstance().log("Pokemon Types pulled with " + count + " records");

                    Message msg = new Message();
                    callback.handleMessage(msg);
                } catch (JSONException jsone) {
                    Util.getInstance().error(jsone.getMessage());
                }
            }
        }, errorListener);

        queue.add(jsonArrayRequest);
    }

    public void pullTypeData(final Handler.Callback callback) {
        String url = BASE_FILE_URL + "types.json";
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
        String url = BASE_FILE_URL + "pokemondb.json";
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
