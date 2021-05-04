package com.example.digipong;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {
    private ImageView btnPlay, btnRanking;
    private Button btnUser;

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

        //Button3
        //get the button
        btnUser = (Button) findViewById(R.id.btnUser);
        //action when click
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
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

    private void openDialog() {
        new AlertDialog.Builder(this)
                .setTitle("To be implemented...")
                .setMessage("Player information")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }






}