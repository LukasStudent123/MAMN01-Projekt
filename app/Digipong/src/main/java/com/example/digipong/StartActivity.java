package com.example.digipong;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {
    private ImageView btnPlay, btnRanking;
    private Button btnUser;
    public String name = "User1";
    public int ranking = 0;
    private EditText editText;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mediaPlayer = MediaPlayer.create(this, R.raw.backgroundgame);
        mediaPlayer.start();

        btnPlay = (ImageView) findViewById(R.id.imgPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        btnRanking = (ImageView) findViewById(R.id.imgRanking);
        btnRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRanking();
            }
        });

        btnUser = (Button) findViewById(R.id.btnUser);
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "to be implemented...", Toast.LENGTH_SHORT)
                        //.show();
                openDialog();
            }
        });

    }


    public void startGame(){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("x", name);
        intent.putExtra("y", ranking);
        mediaPlayer.stop();
        startActivity(intent);
    }


    public void openRanking(){
        Intent intent = new Intent(this, RankActivity.class);
        intent.putExtra("x", name);
        intent.putExtra("y", ranking);
        startActivity(intent);
    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view)
                .setTitle("Enter your name:")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        name = editText.getText().toString();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
        editText = view.findViewById(R.id.username);

        //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT)
                //.show();
    }

}