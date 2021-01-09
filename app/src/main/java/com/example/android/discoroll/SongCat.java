package com.example.android.discoroll;

public enum SongCat {
    NEUTRAL("neutral"),
    HAPPY("happy"),
    HYPER("hyper"),
    SAD("sad"),
    DEPRESSED("depressed");

    private String str;

    private SongCat(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
