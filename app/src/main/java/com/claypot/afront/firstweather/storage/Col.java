package com.claypot.afront.firstweather.storage;

/**
 * Created by afront on 6/4/17.
 */

public class Col {
    private String key;
    private String val;

    public Col(String key, String val) {
        this.key = key;
        this.val = val;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String toString() {
        return this.key + " " + this.val;
    }
}
