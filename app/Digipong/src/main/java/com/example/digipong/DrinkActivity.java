package com.example.digipong;

import android.media.MediaPlayer;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

public class DrinkActivity extends AppCompatActivity {
    private ImageView cup;
    private MediaPlayer mp;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        cup = (ImageView) findViewById(R.id.cup);
        mp = MediaPlayer.create(this, R.raw.drinking);
        mp.start();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mp.stop();
                finish();
            }
        }, 4000);





    }
}