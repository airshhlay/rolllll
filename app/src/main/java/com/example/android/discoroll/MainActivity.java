package com.example.android.discoroll;

import androidx.appcompat.app.AppCompatActivity;

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

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.PlayerApi;
import com.spotify.android.appremote.api.error.CouldNotFindSpotifyApp;
import com.spotify.android.appremote.api.error.NotLoggedInException;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import com.spotify.protocol.types.Track;

import java.util.HashMap;

/*
Code referenced from :
https://developer.spotify.com/documentation/android/quick-start/#next-steps
 */

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "ab9a26a16bde446a87940a4203e3b808";
    private static final int REQUEST_CODE = 1337;
    private static String USER_TOKEN;
    private static final String REDIRECT_URI = "http://com.example.android.discoroll://callback/";
    private SpotifyAppRemote mSpotifyAppRemote;
    // for disco lighting
    private Handler handler = new Handler();
    private Runnable runnable;
    private int delay = 5000;
    private int star_showtime = 400;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void onStart() {
        Log.d("MainActivity", "Program starting...");
        super.onStart();

        AuthorizationRequest.Builder builder = new AuthorizationRequest.
                Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthorizationRequest request = builder.build();

        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
        Log.d("MainActivity", "Request sent");


        // setup layout only after logged in
        setContentView(R.layout.activity_main);

        grid = (GridView) findViewById(R.id.grid_view); // init gridview
        // create custom adapter, set adapter to gridview

        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), characters);
        grid.setAdapter(customAdapter);

        discoView = findViewById(R.id.disco_overlay);
    }

    // disco lighting
    // calls method to change disco lighting
    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);

            }
        }, delay);
        super.onResume();
    }

    private void changeDiscoOverlay() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); // stop handler when activity not visible super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE){
            AuthorizationResponse response = AuthorizationClient.getResponse(
                    resultCode, data);

            switch (response.getType()) {
                case TOKEN:
                    Log.d("MainActivity", "Authorization successful");
                    USER_TOKEN = response.getAccessToken();
                    successfulAuthorization();
                    break;
                case CODE:
                    Log.e("MainActivity", "Code Error");
                    break;
                case ERROR:
                    Log.e("MainActivity", "True Error");
                    break;
                case EMPTY:
                    Log.e("MainActivity", "Empty Field");
                    break;
                case UNKNOWN:
                    Log.e("MainActivity", "Unknown Error");
                    break;
                default:
                    Log.e("MainActivity", "Default reached");
            }
        }
    }

    private void successfulAuthorization(){
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");
                        // Now you can start interacting with App Remote
                        connected();
                    }

                    public void onFailure(Throwable throwable) {
                        // Something went wrong when attempting to connect!
                        if (throwable instanceof CouldNotFindSpotifyApp){
                            // redirect to page to download application
                            Log.e("MainActivity", "Missing Spotify application");
                        } else if (throwable instanceof NotLoggedInException) {
                            // redirect to spotify app to log in
                            Log.e("MainActivity", "Not Logged In");
                        } else {
                            Log.e("MainActivity", throwable.getMessage(), throwable);
                        }
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    private void connected() {
        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);

                        // retrieve the track id
                        String trackUri = track.uri;
                        String trackId = trackUri.split(":")[2];

                        Log.d("MainActivity", trackId);
                        PseudoJson result = HTTPAdapter.getAudioFeatures(USER_TOKEN, trackId);

                        // call letsGoDisco(Genre) here after classifying
                    }
                });
    }

    private void letsGoDisco(String s) {
        // will add a switch case here to determine the transition time + colour scheme

        // show toast based on genre
        showToast(toastMessages.get(s));

        //
    }

    private void showToast(String toastMessage) {
        Toast.makeText(MainActivity.this, toastMessage,
                Toast.LENGTH_SHORT).show();

    }

    // playback controls
    public void playOrPause(View view) {
        try {
            if (mSpotifyAppRemote == null) {
                throw new NullPointerException();
            }
            Log.d("MainActivity", "Play or pause: reached here");
            PlayerApi playerApi = mSpotifyAppRemote.getPlayerApi();
            playerApi
                    .subscribeToPlayerState()
                    .setEventCallback(playerState -> {
                        final Track track = playerState.track;
                        final boolean isPaused = playerState.isPaused;
                        if (isPaused) {
                            // resume
                            playerApi.resume();
                            Log.d("MainActivity", "Resumed the song: " + track.name);
                        } else {
                            playerApi.pause();
                            Log.d("MainActivity", "Paused the song: " + track.name);
                        }
                    });
        } catch (NullPointerException e) {
            Log.e("MainActivity", "Play or Pause: null pointer exception");
        }

    }

    public void skipBackward(View view) {
        try {
            if (mSpotifyAppRemote == null) {
                throw new NullPointerException();
            }
            Log.d("MainActivity", "Skip Backward: reached here");
            PlayerApi playerApi = mSpotifyAppRemote.getPlayerApi();
            playerApi
                    .subscribeToPlayerState()
                    .setEventCallback(playerState -> {
                        playerApi.skipPrevious();
                        //                updateNowPlaying();
                    });
        } catch (NullPointerException e) {
            Log.e("MainActivity", "Skip Backward: null pointer exception");
        }

    }

    public void skipForward(View view) {
        try {
            if (mSpotifyAppRemote == null) {
                throw new NullPointerException();
            }
            Log.d("MainActivity", "Skip Forward: reached here");
            PlayerApi playerApi = mSpotifyAppRemote.getPlayerApi();
            playerApi
                    .subscribeToPlayerState()
                    .setEventCallback(playerState -> {
                        playerApi.skipNext();
//                updateNowPlaying();
                    });
        } catch (NullPointerException e) {
            Log.e("MainActivity", "Skip Forward: null pointer exception");
        }

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
