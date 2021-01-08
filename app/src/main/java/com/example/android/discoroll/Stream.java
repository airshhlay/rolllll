package com.example.android.discoroll;

import android.Manifest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.view.View;


public class Stream extends AppCompatActivity implements Visualizer.OnDataCaptureListener {
    private static final int CAPTURE_SIZE = 256;
    Visualizer visualizer;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        
        visualizer = new Visualizer(mediaPlayer.getAudioSessionId());
        visualizer.setCaptureSize(CAPTURE_SIZE);
        visualizer.setDataCaptureListener(this, Visualizer.getMaxCaptureRate(), true, false);
        visualizer.setEnabled(true);

    }

    @Override
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
        //do whatever. change color change view.
        //discoView.setColor or sth
        
    }

    @Override
    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
        // do whatever . change view

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (visualizer != null) {
            visualizer.setEnabled(false);
            visualizer.release();
            visualizer.setDataCaptureListener(null, 0, false, false);
        }
        super.onPause();
    }
}

