package com.example.digipong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView background, quote;
    private ImageView logo;
    private ArrayList<String> quotes;
    private Handler handler;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        background = (TextView) findViewById(R.id.background);
        quote = (TextView) findViewById(R.id.quote);
        quotes = new ArrayList<>();
        getQ();
        Random rand = new Random();
        quote.setText(quotes.get(rand.nextInt(3)));

        handler = new Handler();
        /*
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Click anywhere to continue", Toast.LENGTH_SHORT)
                        .show();
            }
        }, 2000);
        
         */
    }

    public void onUserInteraction(){
        openApp();
    }

    public void openApp(){
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    public void getQ(){
        quotes.add("Drink along with Digipong");
        quotes.add("Nothing can go wrong with DigiPong");
        quotes.add("In coronatimes - DigiPong is where you belong");
        quotes.add("Bli Salong, kom igång med DigiPong");
    }


}