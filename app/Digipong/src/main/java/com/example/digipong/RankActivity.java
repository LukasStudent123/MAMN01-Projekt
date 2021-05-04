package com.example.digipong;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RankActivity extends AppCompatActivity {
    private ImageView img;
    private TextView one;
    private String name;

    private ArrayList<TextView> textViews;

    // Kanske vi vill byta ut till nån typ av sorterad lista
    private ArrayList<Person> persons;

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

        // Adds all players to a list
        persons = new ArrayList<>();
        persons.add(new Person("Ville", 9, 50));
        persons.add(new Person("Sara", 12, 49));
        persons.add(new Person("Lukas", 10, 48));
        persons.add(new Person("Emma", 11, 47));

        for (int i = 0; i < persons.size(); i++){
            Person person = persons.get(i);
            TextView textView = textViews.get(i);

            String name = person.name;
            int numberOfWins = person.getNumberOfWins();
            int numberOfHits = person.getNumberOfHits();
            textView.setText(name + "   Wins: " + numberOfWins + ", Hits: " + numberOfHits);

            textView.setBackgroundColor(Color.rgb(245, 179, 0));

        }



        /*
        //Formatera i XML
        one = (TextView) findViewById(R.id.one);
        one.setBackgroundColor(Color.rgb(245, 179, 0));

        //To-do lägg till användare i array och printa ut i textview
        one.setText("1: " + name);

         */

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