package com.example.android.discoroll;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.error.CouldNotFindSpotifyApp;
import com.spotify.android.appremote.api.error.NotLoggedInException;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import java.nio.channels.ClosedByInterruptException;

/*
Code referenced from :
https://developer.spotify.com/documentation/android/quick-start/#next-steps
 */

public class MainActivity extends AppCompatActivity {

//    private static final String CLIENT_ID = "ab9a26a16bde446a87940a4203e3b808";
//    private static final int REQUEST_CODE = 1337;
//    private static final String REDIRECT_URI = "http://com.example.android.discoroll://callback/";
//    private static String USER_TOKEN;
//    private SpotifyAppRemote mSpotifyAppRemote;

    Button button;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.simple1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "nextActivity");
                Intent intent = new Intent(getApplicationContext(), StreamActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        Log.d("MainActivity", "Program starting...");
        super.onStart();

//        AuthorizationRequest.Builder builder = new AuthorizationRequest.
//                Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);
//
//        builder.setScopes(new String[]{"streaming"});
//        AuthorizationRequest request = builder.build();
//
//        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
//        Log.d("MainActivity", "Request sent");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == REQUEST_CODE){
//            AuthorizationResponse response = AuthorizationClient.getResponse(
//                    resultCode, data);
//
//            switch (response.getType()) {
//                case TOKEN:
//                    Log.d("MainActivity", "Authorization successful");
//                    USER_TOKEN = response.getAccessToken();
//                    successfulAuthorization();
//                    break;
//                default:
//                    Log.e("MainActivity", "Authorization error");
//            }
//        }
    }

//    private void successfulAuthorization(){
//        ConnectionParams connectionParams =
//                new ConnectionParams.Builder(CLIENT_ID)
//                        .setRedirectUri(REDIRECT_URI)
//                        .showAuthView(true)
//                        .build();
//
//        SpotifyAppRemote.connect(this, connectionParams,
//                new Connector.ConnectionListener() {
//
//                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
//                        mSpotifyAppRemote = spotifyAppRemote;
//                        Log.d("MainActivity", "Connected! Yay!");
//
//                        // Now you can start interacting with App Remote
//                        connected();
//                    }
//
//                    public void onFailure(Throwable throwable) {
//                        // Something went wrong when attempting to connect!
//                        if (throwable instanceof CouldNotFindSpotifyApp){
//                            // redirect to page to download application
//                            Log.e("MainActivity", "Missing Spotify application");
//                        } else if (throwable instanceof NotLoggedInException) {
//                            // redirect to spotify app to log in
//                            Log.e("MainActivity", "Not Logged In");
//                        } else {
//                            Log.e("MainActivity", throwable.getMessage(), throwable);
//                        }
//                    }
//                });
//    }

    @Override
    protected void onStop() {
        super.onStop();
//        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

//    private void connected() {
//        // Play a playlist
//        // mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");
//
//        // Subscribe to PlayerState
//        mSpotifyAppRemote.getPlayerApi()
//                .subscribeToPlayerState()
//                .setEventCallback(playerState -> {
//                    final Track track = playerState.track;
//                    if (track != null) {
//                        Log.d("MainActivity", track.name + " by " + track.artist.name);
//                    }
//                });
//    }
}
