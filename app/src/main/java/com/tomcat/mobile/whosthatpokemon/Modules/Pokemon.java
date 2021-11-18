package com.tomcat.mobile.whosthatpokemon.Modules;

import android.content.ContentValues;
import android.util.Log;

import com.tomcat.mobile.whosthatpokemon.DataTables.PokemonData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Pokemon implements Serializable {
    private String name = "";
    private int generation = 0;
    private String color = "";
    private int number = 0;
    private double height = 0.0;
    private double weight = 0.0;
    private int hp = 0;
    private int attack = 0;
    private int defense = 0;
    private int spatk = 0;
    private int spdef = 0;
    private int speed = 0;
    private int evoNum = 0;
    private int familyId = 0;
    private boolean isRegionalVariant = false;

    public Pokemon() {

    }

    public Pokemon(JSONObject jsonObject) {
        try {
            name = jsonObject.getString(PokemonData.COLUMN_NAME);
            generation = jsonObject.getInt(PokemonData.COLUMN_GENERATION);
            color = jsonObject.getString(PokemonData.COLUMN_COLOR);
            number = jsonObject.getInt(PokemonData.COLUMN_NUMBER);
            height = jsonObject.getDouble(PokemonData.COLUMN_HEIGHT);
            weight = jsonObject.getDouble(PokemonData.COLUMN_WEIGHT);
            hp = jsonObject.getInt(PokemonData.COLUMN_HP);
            attack = jsonObject.getInt(PokemonData.COLUMN_ATTACK);
            defense = jsonObject.getInt(PokemonData.COLUMN_DEFENSE);
            spatk = jsonObject.getInt(PokemonData.COLUMN_SP_ATK);
            spdef = jsonObject.getInt(PokemonData.COLUMN_SP_DEF);
            speed = jsonObject.getInt(PokemonData.COLUMN_SPEED);
            evoNum = jsonObject.getInt(PokemonData.COLUMN_EVO_NUM);
            familyId = jsonObject.getInt(PokemonData.COLUMN_FAMILY_ID);
            isRegionalVariant = jsonObject.getBoolean(PokemonData.COLUMN_IS_REGIONAL_VARIANT);
        } catch (JSONException jsone) {
            Log.e("ERROR", jsone.getMessage());
        }
    }

    public String getName() {
        return name;
    }
    public int getGeneration() {
        return generation;
    }
    public String getColor() {
        return color;
    }
    public int getNumber() {
        return number;
    }
    public double getHeight() {
        return height;
    }
    public double getWeight() {
        return weight;
    }
    public int getHp() {
        return hp;
    }
    public int getAttack() {
        return attack;
    }
    public int getDefense() {
        return defense;
    }
    public int getSpAtk() {
        return spatk;
    }
    public int getSpDef() {
        return spdef;
    }
    public int getSpeed() {
        return speed;
    }
    public int getEvoNum() {
        return evoNum;
    }
    public int getFamilyId() {
        return familyId;
    }
    public boolean getIsRegionalVariant() {
        return isRegionalVariant;
    }

    public void setName(String n) {
        name = n;
    }
    public void setGeneration(int i) {
        generation = i;
    }
    public void setColor(String n) { color = n; }
    public void setNumber(int i) {
        number = i;
    }
    public void setHeight(double d) {
        height = d;
    }
    public void setWeight(double d) {
        weight = d;
    }
    public void setHp(int i ) {
        hp = i;
    }
    public void setAttack(int i) {
        attack = i;
    }
    public void setDefense(int i) {
        defense = i;
    }
    public void setSpAtk(int i) {
        spatk = i;
    }
    public void setSpDef(int i) {
        spdef = i;
    }
    public void setSpeed(int i) {
        speed = i;
    }
    public void setEvoNum(int i) {
        evoNum = i;
    }
    public void setFamilyId(int i) {
        familyId = i;
    }
    public void setIsRegionalVariant(int b) {
        isRegionalVariant = b != 0;
    }

    public String toString() {
        String str = "";
        str += "Name: " + getName();
        str += ", Number: " + getNumber();
        str += ", Generation: " + getGeneration();
        str += ", Color: " + getColor();
        str += ", Height: " + getHeight();
        str += ", Weight: " + getWeight();
        str += ", Hp: " + getHp();
        str += ", Atk: " + getAttack();
        str += ", Def: " + getDefense();
        str += ", SpAtk: " + getSpAtk();
        str += ", SpDef: " + getSpDef();
        str += ", Speed: " + getSpeed();
        str += ", Regional Variant: " + getIsRegionalVariant();

        return str;
    }

    public ContentValues getContentValuesForPokemon() {
        ContentValues cv = new ContentValues();

        cv.put(PokemonData.COLUMN_NUMBER, number);
        cv.put(PokemonData.COLUMN_NAME, name);
        cv.put(PokemonData.COLUMN_GENERATION, generation);
        cv.put(PokemonData.COLUMN_HEIGHT, height);
        cv.put(PokemonData.COLUMN_WEIGHT, weight);
        cv.put(PokemonData.COLUMN_HP, hp);
        cv.put(PokemonData.COLUMN_ATTACK, attack);
        cv.put(PokemonData.COLUMN_DEFENSE, defense);
        cv.put(PokemonData.COLUMN_SP_ATK, spatk);
        cv.put(PokemonData.COLUMN_SP_DEF, spdef);
        cv.put(PokemonData.COLUMN_SPEED, speed);
        cv.put(PokemonData.COLUMN_EVO_NUM, evoNum);
        cv.put(PokemonData.COLUMN_FAMILY_ID, familyId);
        cv.put(PokemonData.COLUMN_COLOR, color);
        cv.put(PokemonData.COLUMN_IS_REGIONAL_VARIANT, isRegionalVariant);

        return cv;
    }
}
