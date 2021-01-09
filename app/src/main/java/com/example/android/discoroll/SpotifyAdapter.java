package com.example.android.discoroll;

import android.content.Context;
import android.util.Log;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.PlayerApi;
import com.spotify.android.appremote.api.error.CouldNotFindSpotifyApp;
import com.spotify.android.appremote.api.error.NotLoggedInException;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

public class SpotifyAdapter {
    private static final String CLIENT_ID = "ab9a26a16bde446a87940a4203e3b808";
    private static final String REDIRECT_URI = "http://com.example.android.discoroll://callback/";
    private static SpotifyAppRemote mSpotifyAppRemote;

    private static PlayerState currentState;
    private static PseudoJson currentFeatures;

    public static void connectToApp(Utility utility, Context context){
        ConnectionParams connectionParams =
            new ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .showAuthView(true)
                .build();

        SpotifyAppRemote.connect(context, connectionParams,
            new Connector.ConnectionListener() {

                public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                    mSpotifyAppRemote = spotifyAppRemote;
                    Log.d("Spotify", "Connected!");
                    // Now you can start interacting with App Remote
                    connected(utility, context);
                }

                public void onFailure(Throwable throwable) {
                    // Something went wrong when attempting to connect!
                    if (throwable instanceof CouldNotFindSpotifyApp){
                        // redirect to page to download application
                        Log.e("Spotify", "Missing Spotify app");
                    } else if (throwable instanceof NotLoggedInException) {
                        // redirect to spotify app to log in
                        Log.e("Spotify", "Not Logged In");
                    } else {
                        Log.e("Spotify", throwable.getMessage(), throwable);
                    }
                }
            });
    }

    public static void disconnectFromApp(){
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    private static void connected(Utility utility, Context context) {
        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
            .subscribeToPlayerState()
            .setEventCallback(playerState -> {
                currentState = playerState;
                final Track track = playerState.track;
                Log.d("Spotify", track.uri);
                if (track != null) {
                    Log.d("Spotify", track.name + " by " + track.artist.name);
                    // retrieve the track id
                    String trackId = track.uri.split(":")[2];
                    currentFeatures = HTTPAdapter.getAudioFeatures(trackId);

                    // TODO: after getting the features, call utility.letsGoDisco(MainActivity.this, SongCat sc);
                    // need you to help me select a songcat!
                    Log.d("FEATURES", currentFeatures.toString());
                    SongCat sc = utility.determineSongCat(currentFeatures);
                    Log.d("SONG CAT", sc.toString());
                    utility.letsGoDisco(context, sc);
                }
            });
    }

    public static void playOrPause() {
        final boolean isPlaying = !currentState.isPaused;
        if (isPlaying) {
            mSpotifyAppRemote.getPlayerApi().pause();
        } else {
            mSpotifyAppRemote.getPlayerApi().resume();
        }
    }

    public static void skipBackwards() {
        mSpotifyAppRemote.getPlayerApi().skipPrevious();
    }

    public static void skipForward() {
        mSpotifyAppRemote.getPlayerApi().skipNext();
    }
}
