package com.example.digipong;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class RankActivity extends AppCompatActivity {
    private ImageView img;
    private TextView one;
    private String name;
    private Random rand;

    private ArrayList<TextView> textViews;

    private ArrayList<Person> persons;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

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
        persons.add(new Person("Me", rand.nextInt(10), rand.nextInt(50)));

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