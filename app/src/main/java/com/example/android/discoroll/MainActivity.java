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

    private static final String CLIENT_ID = "ab9a26a16bde446a87940a4203e3b808";
    private static final int REQUEST_CODE = 1337;
    private static String USER_TOKEN;
    private static final String REDIRECT_URI = "http://com.example.android.discoroll://callback/";

    GridView grid;

    private Handler handler = new Handler();
    private Runnable runnable;
    private Utility utility;

    private int delay = 5000;


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

        View discoView = findViewById(R.id.disco_overlay);
        View rainView = findViewById(R.id.rain_overlay);
        View starView = findViewById(R.id.star_overlay);

        utility = new Utility(discoView, rainView, starView, handler);
    }

    @Override
    protected void onStart() {
        Log.d("MainActivity", "Program starting...");
        super.onStart();
        SpotifyAdapter.connectToApp(utility, this);
    }

    // disco lighting
    // calls method to change disco lighting
    @Override
    protected void onResume() {
        utility.changeDiscoOverlay();
        super.onResume();
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

//    public void updateNowPlaying() {
//        mSpotifyAppRemote.getPlayerApi()
//                .subscribeToPlayerState()
//                .setEventCallback(playerState -> {
//                    final Track track = playerState.track;
//                    if (track != null) {
//                        Log.d("MainActivity", "In updateNowPlaying, track detected");
//                        TextView textView = findViewById(R.id.now_playing);
//                        // update now playing
//                        textView.setText("dj is playing: " + track.name);
//                    }
//                });
//
//
//    }
}
