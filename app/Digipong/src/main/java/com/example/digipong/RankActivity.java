package com.example.digipong;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class RankActivity extends AppCompatActivity {
    private ImageView img;
    private TextView one;
    private String name;
    private int ranking;
    private Random rand;

    private StartActivity sa = new StartActivity();

    private ArrayList<TextView> textViews;

    private ArrayList<Person> persons;

    private MediaPlayer mediaPlayer;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        mediaPlayer = MediaPlayer.create(this, R.raw.tadasound);
        mediaPlayer.start();

        name = getIntent().getStringExtra("x");
        ranking = getIntent().getIntExtra("y", 0);

        // Adds all textViews on screen to a combined list
        textViews = new ArrayList<>();
        textViews.add(findViewById(R.id.one));
        textViews.add(findViewById(R.id.two));
        textViews.add(findViewById(R.id.three));
        textViews.add(findViewById(R.id.four));
        textViews.add(findViewById(R.id.five));

        rand = new Random();

        // Adds all players to a list
        persons = new ArrayList<>();
        persons.add(new Person("Ville", rand.nextInt(10), rand.nextInt(50)));
        persons.add(new Person("Sara", rand.nextInt(10), rand.nextInt(50)));
        persons.add(new Person("Lukas", rand.nextInt(10), rand.nextInt(50)));
        persons.add(new Person("Emma", rand.nextInt(10), rand.nextInt(50)));
        persons.add(new Person(name, rand.nextInt(10), rand.nextInt(50)));


        //Sort persons by Number of wins
        persons.sort(Comparator.comparing(Person::getNumberOfWins).reversed());

        for (int i = 0; i < persons.size(); i++){
            Person person = persons.get(i);
            TextView textView = textViews.get(i);

            String name = person.name;
            int numberOfWins = person.getNumberOfWins();
            int numberOfHits = person.getNumberOfHits();
            textView.setText(name + "   Wins: " + numberOfWins + ", Hits: " + numberOfHits);

            if( i % 2 == 0){
                textView.setBackgroundColor(Color.rgb(245, 179, 0));
            } else {
                textView.setBackgroundColor(Color.rgb(250, 200, 40));
            }


        }

        final Handler h = new Handler();
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                ImageView gif = findViewById(R.id.confettiGif);
                gif.setVisibility(View.INVISIBLE);
            }
        };
        h.postDelayed(r, 3000);

    }

    private class Person{
        private String name;
        private int numberOfWins;
        private int numberOfHits;

        private Person(String name, int numberOfWins, int numberOfHits){
            this.name = name;
            this.numberOfWins = numberOfWins;
            this.numberOfHits = numberOfHits;
        }

        public String getName(){
            return name;
        }

        public int getNumberOfWins(){
            return numberOfWins;
        }

        public int getNumberOfHits() {
            return numberOfHits;
        }
    }



}