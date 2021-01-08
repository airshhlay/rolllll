package com.example.android.discoroll;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.graphics.Color;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.content.Intent;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Random;

/*
Code referenced from :
https://developer.spotify.com/documentation/android/quick-start/#next-steps
 */

public class MainActivity extends AppCompatActivity {
    // for disco lighting
    private Handler handler = new Handler();
    private Runnable runnable;
    private int delay = 5000;
    private int star_showtime = 400;
    private Random random;

    GridView grid;
    View discoView;

    // Color reference taken from https://www.schemecolor.com/disco-dance.php
    private int discoColors[] = {Color.rgb(15, 192, 252),
            Color.rgb(123,29,175), Color.rgb(255, 47, 185),
            Color.rgb(212, 155, 71), Color.rgb(27,54,73)};

    private int currColorIndex = 0; // for cycling through disco colours


    private HashMap<String, String> toastMessages = new HashMap<String, String>() {{
        put("sad", "AIN'T NO CRYIN' IN THE CLUB AY AY~");
        put("depressed", "...You okay bro?");
        put("happy", "AY AY AY LET'S GEDDIT");
        put("hyper", "PUT YOUR HANDS UP!!!!!!!!!@%");
    }};

    int characters[] = {
            R.drawable.squidward, R.drawable.squidward, R.drawable.squidward,
            R.drawable.squidward, R.drawable.squidward, R.drawable.squidward,
            R.drawable.squidward, R.drawable.squidward, R.drawable.squidward,
            R.drawable.squidward, R.drawable.squidward, R.drawable.squidward,};

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HTTPAdapter.clientAuth();

        // TODO: setup actual layout only after logged in
        setContentView(R.layout.activity_main);

        grid = (GridView) findViewById(R.id.grid_view); // init gridview
        // create custom adapter, set adapter to gridview

        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), characters);
        grid.setAdapter(customAdapter);

        discoView = findViewById(R.id.disco_overlay);

        random = new Random();
    }

    @Override
    protected void onStart() {
        Log.d("MainActivity", "Program starting...");
        super.onStart();
        SpotifyAdapter.connectToApp(this);
    }

    // disco lighting
    // calls method to change disco lighting
    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                changeDiscoOverlay();
            }
        }, delay);
        super.onResume();
    }

    private void changeDiscoOverlay() {
        if (discoView.getVisibility() == View.INVISIBLE) {
            enableDiscoOverlay();
        }

        int colorIndex = random.nextInt(discoColors.length);
        discoView.setBackgroundColor(discoColors[colorIndex]);

    }

    private void disableDiscoOverlay() {
        discoView.setVisibility(View.INVISIBLE);
    }

    private void enableDiscoOverlay() {
        discoView.setVisibility(View.VISIBLE);
    }

    private void setSadDiscoOverlay() {
        if (discoView.getVisibility() == View.INVISIBLE) {
            enableDiscoOverlay();
        }

        discoView.setBackgroundColor(Color.BLACK);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); // stop handler when activity not visible super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAdapter.disconnectFromApp();
    }

    // determines the mood of the currently playing song and sets up UI animations accordingly
    private void letsGoDisco(String s) {
        // will add a switch case here to determine the transition time + colour scheme

        // show toast based on genre
        showToast(toastMessages.get(s));

        // if happy, start disco + play stars for 3 seconds
        // if hyper, start disco (shorter interval) + play stars for 3 seconds
        // if sad, start rain
        // if depressed, start rain + set disco overlay to black :(
        // if neutral, disable disco overlay and disable rain (if have)
    }

    private void showToast(String toastMessage) {
        Toast.makeText(MainActivity.this, toastMessage,
                Toast.LENGTH_SHORT).show();

    }

    // playback controls
    public void playOrPause(View view) {
        SpotifyAdapter.playOrPause();
    }

    public void skipBackward(View view) {
        Log.d("Main", "Back Pressed");
        SpotifyAdapter.skipBackwards();
    }

    public void skipForward(View view) {
        Log.d("Main", "Forward Pressed");
        SpotifyAdapter.skipForward();
    }
}
