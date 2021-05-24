package com.example.digipong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Debug;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OnEdgeActivity extends AppCompatActivity implements View.OnLongClickListener {
    private MediaRecorder recorder;
    private Timer timer = new Timer();
    private ImageView cup;
    private MediaPlayer swirl;
    private CountDownTimer cdt;
    final Handler handler = new Handler();
    private static final String DEBUG_TAG = "Velocity";
    private VelocityTracker mVelocityTracker = null;


    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onedge);

        swirl = MediaPlayer.create(this, R.raw.spinning);
        cup = (ImageView) findViewById(R.id.cup);
        swirl.start();

        boolean p1turn = getIntent().getBooleanExtra("p1turn", true);
        int onEdge;
        int onEdge_right;
        if(p1turn) {
            //cup.setImageResource(bild på blå mugg)

            onEdge = R.drawable.blue_onedge;
            onEdge_right = R.drawable.blue_onedge_right;
        } else{
            onEdge = R.drawable.onedge;
            onEdge_right = R.drawable.onedge_right;
        }

        cup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swirl.stop();
                Intent resultIntent = getIntent();
                resultIntent.putExtra("result", true);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        //startRecording();
        //int i = recorder.getMaxAmplitude();


        cdt = new CountDownTimer(3500, 500) {
            boolean b = false;

            public void onTick(long millisUntilFinished) {
                if (b == false) {
                    cup.setImageResource(onEdge);
                    b = true;
                } else if (b == true) {
                    cup.setImageResource(onEdge_right);
                    b = false;
                }
            }

            public void onFinish() {
                swirl.stop();
                Intent resultIntent = getIntent();
                resultIntent.putExtra("result", false);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        }.start();

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}



//timer.schedule(new cupRotation(), 0, 500);

/*
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 100;
    private static final String LOG_TAG = "AudioRecordTest";
    private static String fileName = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private boolean permissionWriteExternalStorageAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();
    }


    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        recorder.start();

    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onedge);
        cup = (ImageView) findViewById(R.id.cup);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);

        mp = MediaPlayer.create(this, R.raw.spinning);
        mp.start();

        //startRecording();
        //int i = recorder.getMaxAmplitude();


        cdt = new CountDownTimer(3500, 500) {
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished%1000 == 0){
                    cup.setImageResource(R.drawable.cupwithball);
                } else {
                    cup.setImageResource(R.drawable.cuprotation);
                }
            }
            public void onFinish() {
                mp.stop();
                //stopRecording();
                finish();
            }
        }.start();

    }

    //https://www.codota.com/code/java/methods/android.media.MediaRecorder/getMaxAmplitude ??

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

     */