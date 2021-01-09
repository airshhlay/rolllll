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


    private View discoView;
    private View rainView;
    private View starView;
    private Handler handler;

    Runnable runnable;
    int delay;

    // Color reference taken from https://www.schemecolor.com/disco-dance.php
    private int discoColors[] = {Color.rgb(15, 192, 252),
            Color.rgb(123,29,175), Color.rgb(255, 47, 185),
            Color.rgb(212, 155, 71), Color.rgb(27,54,73)};

    private int happyInterval = 5000;
    private int hyperInterval = 3000;

    private static HashMap<SongCat, String> toastMessages = new HashMap<SongCat, String>() {{
        put(SongCat.SAD, "Should you be playing this at the disco?");
        put(SongCat.DEPRESSED, "...You okay bro?");
        put(SongCat.HAPPY, "AY AY AY LET'S GEDDIT");
        put(SongCat.HYPER, "PUT YOUR HANDS UP!!!!!!!!!@%");
        put(SongCat.NEUTRAL, "AIN'T NO CRYIN' IN THE CLUB AY AY~");
    }};

    public Utility(View discoView, View rainView, View starView, Handler handler) {

        this.discoView = discoView;
        this.rainView = rainView;
        this.starView = starView;
        this.handler = handler;
        random = new Random();
    }


    public void changeDiscoOverlay() {
        if (discoView.getVisibility() == View.VISIBLE) {
            int colorIndex = random.nextInt(discoColors.length);
            discoView.setBackgroundColor(discoColors[colorIndex]);
        }
    }

    public void startDiscoLights(SongCat sc) {
        delay = sc == SongCat.HAPPY ? happyInterval : hyperInterval;
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                enableDiscoOverlay();
                changeDiscoOverlay();
            }
        }, delay);
    }

    public void disableDiscoOverlay() {
        if (discoView.getVisibility() == View.VISIBLE) {
            discoView.setVisibility(View.INVISIBLE);
        }
    }

    public void enableDiscoOverlay() {
        if (discoView.getVisibility() != View.VISIBLE) {
            discoView.setVisibility(View.VISIBLE);
        }
    }

    public void setSadDiscoOverlay() {
        if (discoView.getVisibility() == View.INVISIBLE) {
            enableDiscoOverlay();
        }
        discoView.setBackgroundColor(Color.BLACK);
    }

    public void enableRain() {
        if (rainView.getVisibility() != View.VISIBLE) {
            rainView.setVisibility(View.VISIBLE);
        }
    }

    public void disableRain () {
        if (rainView.getVisibility() == View.VISIBLE) {
            rainView.setVisibility(View.INVISIBLE);
        }
    }

    public void disableAll() {
        disableDiscoOverlay();
        disableRain();
        disableStars();
    }

    public void enableStars() {
        if (starView.getVisibility() != View.VISIBLE) {
            starView.setVisibility(View.VISIBLE);
        }
    }

    public void disableStars() {
        if (starView.getVisibility() == View.VISIBLE) {
            starView.setVisibility(View.INVISIBLE);
        }
    }

    // determines the mood of the currently playing song and sets up UI animations accordingly
    public void letsGoDisco(Context context, SongCat sc) {
        disableAll();
        // will add a switch case here to determine the transition time + colour scheme



        // if happy, start disco + play stars for 3 seconds
        // if hyper, start disco (shorter interval) + play stars for 3 seconds
        // if sad, start rain
        // if depressed, start rain + set disco overlay to black :(
        // if neutral, disable disco overlay and disable rain (if have)
        switch (sc) {
            case HAPPY:
                startDiscoLights(sc);
                break;
            case HYPER:
                startDiscoLights(sc);
                enableStars();
                break;
            case SAD:
                enableRain();
                break;
            case DEPRESSED:
                enableRain();
                setSadDiscoOverlay();
                break;
            case NEUTRAL:
                break;
        }


        // show toast based on genre
        showToast(context, toastMessages.get(sc));
    }

    private void showToast(Context context, String toastMessage) {
        Toast.makeText(context, toastMessage,
                Toast.LENGTH_SHORT).show();
    }
}
