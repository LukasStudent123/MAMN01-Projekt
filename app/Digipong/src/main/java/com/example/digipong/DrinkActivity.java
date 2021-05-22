package com.example.digipong;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

public class DrinkActivity extends AppCompatActivity implements SensorEventListener{
    private ImageView cup;
    private MediaPlayer mp;
    final Handler handler = new Handler();
    private SensorManager sm;
    private Sensor accSensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sm.registerListener((SensorEventListener) this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);

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
        }, 5000);

    }

    public void onSensorChanged(SensorEvent event) {
        /*
        float zVal = event.values[2];
        if(Math.round((double) zVal) < -5 ){
            Toast.makeText(getApplicationContext(), "Drinking..." , Toast.LENGTH_LONG)
                    .show();
        }

         */
        Toast.makeText(getApplicationContext(), "Drinking..." , Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}