package com.example.digipong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class OnEdgeActivity extends AppCompatActivity {
    private MediaRecorder mediaRecorder;
    private Timer timer = new Timer();
    private ImageView cup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onedge);
        cup = (ImageView) findViewById(R.id.cup);
/*
        if (ActivityCompat.checkSelfPermission(OnEdgeActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(OnEdgeActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, BuildDev.RECORD_AUDIO);

        } else {

            startRecording();

        }

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setMaxDuration(1000);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
        int res = mediaRecorder.getMaxAmplitude();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaRecorder.stop();
            }
        }, 5000);
        */

        /*
        String blowing = String.valueOf(isBlowing());
        Toast.makeText(getApplicationContext(), blowing , Toast.LENGTH_SHORT)
                .show();

         */
        //timer.schedule(new cupRotation(), 0, 500);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    finish();
            }
        }, 5000);

    }

    //https://gist.github.com/h4ck4life/6433506
    public boolean isBlowing() {
        boolean recorder=true;

        int minSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);
        AudioRecord ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000,AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT,minSize);

        short[] buffer = new short[minSize];

        ar.startRecording();
        while(recorder)
        {
            ar.read(buffer, 0, minSize);
            for (short s : buffer) {
                if (Math.abs(s) > 27000) {  //DETECT VOLUME (IF I BLOW IN THE MIC)
                    int blow_value = Math.abs(s);

                    Toast.makeText(getApplicationContext(), blow_value , Toast.LENGTH_LONG)
                            .show();

                    ar.stop();
                    recorder=false;

                    return true;
                }
            }
        }
        return false;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1234: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isBlowing();
                } else {
                    Log.d("TAG", "permission denied by user");
                }
                return;
            }
        }
    }


    class cupRotation extends TimerTask{
        boolean b = true;
        public void run() {
            if(b) {
                cup.setImageResource(R.drawable.cuprotation);
                b = false;
            } else {
                cup.setImageResource(R.drawable.cupwithball);
                b = true;
            }
        }
    }
}
