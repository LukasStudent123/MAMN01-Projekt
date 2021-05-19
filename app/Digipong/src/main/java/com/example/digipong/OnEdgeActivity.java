package com.example.digipong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Debug;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
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

public class OnEdgeActivity extends AppCompatActivity {
    private MediaRecorder recorder;
    private Timer timer = new Timer();
    private ImageView cup;
    private MediaPlayer mp;
    private CountDownTimer cdt;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 100;
    private static final String LOG_TAG = "AudioRecordTest";
    private static String fileName = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private boolean permissionWriteExternalStorageAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    private ParcelFileDescriptor[] descriptors;
    private ParcelFileDescriptor parcelRead = new ParcelFileDescriptor(descriptors[0]);
    private ParcelFileDescriptor parcelWrite = new ParcelFileDescriptor(descriptors[1]);

    /*
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
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                Log.d(LOG_TAG, "RecordAcc() OK");//
                break;
        }
        if (!permissionToRecordAccepted ) finish();

        switch (requestCode){
            case REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION:
                permissionWriteExternalStorageAccepted  = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                Log.d(LOG_TAG, "WriteAcc() OK");
                break;
        }
        if (!permissionWriteExternalStorageAccepted ) finish();

        Log.d(LOG_TAG, "grantResult" + grantResults[0] + grantResults[1]);
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
        try {
            descriptors = ParcelFileDescriptor.createPipe();
        } catch (IOException e) {
            Log.e(LOG_TAG, "createPipe(): failed");
        }
        recorder.setOutputFile(parcelWrite.getFileDescriptor());
        //recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        Log.e(LOG_TAG, "start() initialized");
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

        mp = MediaPlayer.create(this, R.raw.spinning);
        mp.start();

        startRecording();
        int i = recorder.getMaxAmplitude();
        Log.i(LOG_TAG, "maxAmp" + i);


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

}


//timer.schedule(new cupRotation(), 0, 500);