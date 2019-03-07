package com.avkd.humible;

import com.google.gson.Gson;

import org.json.JSONObject;

import androidx.annotation.NonNull;

public class Record {

    private int time;
    private float value;

    public Record(int time, float value) {
        this.time = time;
        this.value = value;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @NonNull
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
