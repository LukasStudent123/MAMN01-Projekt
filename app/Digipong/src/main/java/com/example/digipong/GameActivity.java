package com.example.digipong;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

public class GameActivity extends AppCompatActivity {
    private ImageView table;
    private ImageView[] enemycups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //table = (ImageView) findViewById(R.id.table);
        this.enemycups = new ImageView[6];

    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}