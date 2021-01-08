package com.example.android.discoroll;

import java.util.HashMap;
import java.util.Set;

import android.util.Log;

import androidx.annotation.NonNull;

public class PseudoJson {
    private HashMap<String, String> map;

    public PseudoJson(){
        map = new HashMap<>();
    }

    public void populateJson(String input) {
        String[] inputSplit = input.split(",");
        for (String x : inputSplit) {
            if (!x.contains(":"))
                continue;

            String[] xSplit = x.split(":");
            // process key
            String key = xSplit[0];
            key = key.replace("\"", "");
            key = key.replace("{", "");
            key = key.replace("}", "");
            key = key.trim();

            String value = xSplit[1];
            value = value.replace("\"", "");
            value = value.replace("}", "");
            value = value.trim();

            map.put(key, value);
        }
    }

    public String getValue(String key){
        return map.get(key);
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Set<String> keys = this.map.keySet();
        for (String x : keys) {
            sb.append(x + " : " + map.get(x) + "\n");
        }
        return sb.toString();
    }
}
