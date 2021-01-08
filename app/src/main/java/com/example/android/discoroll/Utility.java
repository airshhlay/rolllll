package com.example.android.discoroll;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Random;

public class Utility {

    // for disco lighting
    private int star_showtime = 400;
    private Random random;


    View discoView;

    // Color reference taken from https://www.schemecolor.com/disco-dance.php
    private int discoColors[] = {Color.rgb(15, 192, 252),
            Color.rgb(123,29,175), Color.rgb(255, 47, 185),
            Color.rgb(212, 155, 71), Color.rgb(27,54,73)};


    private static HashMap<String, String> toastMessages = new HashMap<String, String>() {{
        put("sad", "AIN'T NO CRYIN' IN THE CLUB AY AY~");
        put("depressed", "...You okay bro?");
        put("happy", "AY AY AY LET'S GEDDIT");
        put("hyper", "PUT YOUR HANDS UP!!!!!!!!!@%");
    }};

    public Utility(View discoView) {

        this.discoView = discoView;
        random = new Random();
    }

    public void controlDiscoLighting() {

    }

    public void changeDiscoOverlay() {
        if (discoView.getVisibility() == View.INVISIBLE) {
            enableDiscoOverlay();
        }

        int colorIndex = random.nextInt(discoColors.length);
        discoView.setBackgroundColor(discoColors[colorIndex]);

    }

    public void disableDiscoOverlay() {
        discoView.setVisibility(View.INVISIBLE);
    }

    public void enableDiscoOverlay() {
        discoView.setVisibility(View.VISIBLE);
    }

    public void setSadDiscoOverlay() {
        if (discoView.getVisibility() == View.INVISIBLE) {
            enableDiscoOverlay();
        }

        discoView.setBackgroundColor(Color.BLACK);
    }

    // determines the mood of the currently playing song and sets up UI animations accordingly
    private void letsGoDisco(Context context, String s) {
        // will add a switch case here to determine the transition time + colour scheme

        // show toast based on genre
        showToast(context, toastMessages.get(s));

        // if happy, start disco + play stars for 3 seconds
        // if hyper, start disco (shorter interval) + play stars for 3 seconds
        // if sad, start rain
        // if depressed, start rain + set disco overlay to black :(
        // if neutral, disable disco overlay and disable rain (if have)
    }

    private void showToast(Context context, String toastMessage) {
        Toast.makeText(context, toastMessage,
                Toast.LENGTH_SHORT).show();

    }



}
