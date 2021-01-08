package com.example.android.discoroll;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.PlayerApi;
import com.spotify.android.appremote.api.error.CouldNotFindSpotifyApp;
import com.spotify.android.appremote.api.error.NotLoggedInException;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.types.Track;

/*
Code referenced from :
https://developer.spotify.com/documentation/android/quick-start/#next-steps
 */

public class MainActivity extends AppCompatActivity {
    GridView grid;

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
        setContentView(R.layout.activity_main);

        grid = (GridView) findViewById(R.id.grid_view); // init gridview
        // create custom adapter, set adapter to gridview

        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), characters);
        grid.setAdapter(customAdapter);
    }

    @Override
    protected void onStart() {
        Log.d("MainActivity", "Program starting...");
        super.onStart();
        SpotifyAdapter.connectToApp(this);
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

}
