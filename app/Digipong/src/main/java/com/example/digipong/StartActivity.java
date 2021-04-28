package com.example.digipong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {
    private ImageView btnPlay, btnRanking;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        btnRanking = (ImageView) findViewById(R.id.imgRanking);

        //Button1
        //get the button
        btnPlay = (ImageView) findViewById(R.id.imgPlay);
        //action when click
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        //Button2
        //get the button
        btnRanking = (ImageView) findViewById(R.id.imgRanking);
        //action when click
        btnRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRanking();
            }
        });


    }


    public void startGame(){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void openRanking(){
        Intent intent = new Intent(this, RankActivity.class);
        startActivity(intent);
    }




}