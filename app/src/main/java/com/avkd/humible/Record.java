package com.avkd.humible;

public class Record {

    int time;
    float value;

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
}
