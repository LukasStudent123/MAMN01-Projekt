package com.example.digipong;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class DrinkingActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sm;
    private Sensor accSensor;
    private MediaPlayer mp;
    private ImageView cup;
    final Handler handler = new Handler();
    private TextView pos;
    boolean p1turn;

    private ProgressBar pb;
    private int pbStatus = 0;
    private TextView procenatge;

    static final float ALPHA = 0.25f; // if ALPHA = 1 OR 0, no filter applies.
    private float[] accSensorVals;


    protected void onResume() {
        super.onResume();
        sm.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinking);
        cup = (ImageView) findViewById(R.id.drinkingcup);
        pos = (TextView) findViewById(R.id.pos);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener((SensorEventListener) this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);

        mp = MediaPlayer.create(this, R.raw.pouring);

        p1turn = getIntent().getBooleanExtra("p1turn", true);


        if(p1turn) {
            cup.setImageResource(R.drawable.blue_filledcup);
        } else{
            cup.setImageResource(R.drawable.filledcup);
        }

        pb = (ProgressBar) findViewById(R.id.progressBarDrinking);
        procenatge = (TextView) findViewById(R.id.drinkingPercentage);
        procenatge.setText("0 %");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            accSensorVals = lowPass(event.values.clone(), accSensorVals);
        }

        float x = Math.round(accSensorVals[0]);
        float y = Math.round(accSensorVals[1]);
        float z = Math.round(accSensorVals[2]);

        pos.setText("Y: " + y + " z: " + z);

        tiltChange(x, y, z);
    }

    protected float[] lowPass(float[] input, float[] output ) {
        if ( output == null ) return input;
        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    public void tiltChange(float xVal, float yVal, float zVal){

        if(yVal > 8 && zVal > 0 ){


            mp.start();


            /*
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mp.stop();
                    Intent resultIntent = getIntent();
                    resultIntent.putExtra("result", true);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            }, 7000);

             */
            if(pbStatus <100){
                pbStatus++;
                pb.setProgress(pbStatus);
                procenatge.setText(pbStatus + " %");
            } else{
                if(p1turn) {
                    cup.setImageResource(R.drawable.blue_emptycup);
                } else{
                    cup.setImageResource(R.drawable.emptycup);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent resultIntent = getIntent();
                        resultIntent.putExtra("result", true);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                }, 2000);
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}