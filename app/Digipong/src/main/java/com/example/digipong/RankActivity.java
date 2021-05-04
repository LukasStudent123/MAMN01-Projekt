package com.example.digipong;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class RankActivity extends AppCompatActivity {
    private ImageView img;
    private TextView one;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        //Formatera i XML
        one = (TextView) findViewById(R.id.one);
        one.setBackgroundColor(Color.rgb(245, 179, 0));

        //To-do lägg till användare i array och printa ut i textview
        one.setText("1: " + name);

    }



}