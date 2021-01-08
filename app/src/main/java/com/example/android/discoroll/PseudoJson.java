package com.example.android.discoroll;

import java.util.HashMap;

import android.util.Log;

public class PseudoJson {
    private HashMap<String, String> map;

    public PseudoJson(){
        map = new HashMap<>();
    }

    public PseudoJson populateJson(String input) {
        PseudoJson result = new PseudoJson();
        String[] inputSplit = input.split(",");
        for (String x : inputSplit) {
            if (!x.contains(":"))
                continue;
            String[] xSplit = x.split(" : ");
            // process key
            String key = xSplit[0];
            key = key.replace("\"", "");
            key = key.replace("{", "");
            key = key.replace("}", "");
            key = key.trim();

            String value = xSplit[1];
            value = value.trim();
            value = value.replace("\"", "");
            result.map.put(key, value);
        }
        Log.d("PseudoJson", result.map.size() + " entries recorded");
        return result;
    }

    public String getValue(String key){
        return map.get(key);
    }
}
