package com.example.digipong;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
    private ImageView table;
    private TextView rankingText;
    private List<ImageView> enemycups = new ArrayList<>();
    private List<ImageView> playercups = new ArrayList<>();
    private List<Point> enemycupspos = new ArrayList<>();
    private List<Point> playercupspos = new ArrayList<>();
    private ImageView ball;
    private GestureDetectorCompat mDetector;
    private static final int MIN_DISTANCE_MOVED = 50;
    private static final float MIN_TRANSLATION = 0;
    private static final float FRICTION = 1.1f;
    private int maxTranslationX;
    private int maxTranslationY;
    private RelativeLayout mMainLayout;
    private float ballx;
    private float bally;
    private int ranking;
    private int totalHits;
    private Vibrator vibrator;
    final Handler handler = new Handler();
    public static final float PIXELS_PER_SECOND = 10000;
    private MediaPlayer mediaPlayer;
    private int counter = 0;
    private boolean p1turn = true;
    private int p1score = 0;
    private int p2score = 0;
    private String p1name;
    public int edgecup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mediaPlayer = MediaPlayer.create(this, R.raw.onhit);
        ball = findViewById(R.id.pingpongball);
        addEnemyCups();
        addPlayerCups();

        ranking = 0;
        rankingText = findViewById(R.id.score);
        totalHits = getIntent().getIntExtra("y", 0);
        getIntent().removeExtra("y");
        p1name = getIntent().getStringExtra("x");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        }

        //mMainLayout = (RelativeLayout) findViewById(R.layout.activity_game);
        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(this,this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);
        //table = (ImageView) findViewById(R.id.table);

        Toast.makeText(getApplicationContext(), "x & y val" + ball.getX() + ball.getY(), Toast.LENGTH_SHORT)
                .show();

    }

    private void addEnemyCups() {
        enemycups.add(findViewById(R.id.enemycup1));
        enemycups.add(findViewById(R.id.enemycup2));
        enemycups.add(findViewById(R.id.enemycup3));
        enemycups.add(findViewById(R.id.enemycup4));
        enemycups.add(findViewById(R.id.enemycup5));
        enemycups.add(findViewById(R.id.enemycup6));
    }

    private void addPlayerCups() {
        playercups.add(findViewById(R.id.mycup4));
        playercups.add(findViewById(R.id.mycup5));
        playercups.add(findViewById(R.id.mycup6));
        playercups.add(findViewById(R.id.mycup3));
        playercups.add(findViewById(R.id.mycup2));
        playercups.add(findViewById(R.id.mycup1));
    }

    private void addEnemyCupsPos() {
        for (ImageView cup : enemycups) {
            Point p = new Point(cup.getLeft(), cup.getTop());
            enemycupspos.add(p);
        }
    }

    private void addPlayerCupsPos() {
        for (ImageView cup : playercups) {
            Point p = new Point(cup.getLeft(), cup.getTop());
            playercupspos.add(p);
        }
    }

    private int getRanking() {
        return ranking;
    }

    private void reset() {
        String winner;
        if (p1turn) {
            winner = p1name;
        } else {
            winner = "Player 2";
        }
        Toast.makeText(getApplicationContext(), winner + " wins!", Toast.LENGTH_SHORT)
                .show();
        enemycups.clear();
        playercups.clear();
        enemycupspos.clear();
        playercupspos.clear();
        addEnemyCups();
        addPlayerCups();
        addEnemyCupsPos();
        addPlayerCupsPos();
        for (ImageView cup : enemycups) {
            cup.setImageResource(R.drawable.filledcup);
            cup.setVisibility(View.VISIBLE);
        }
        this.ranking = 0;
        for (ImageView cup : playercups) {
            cup.setImageResource(R.drawable.filledcup);
            cup.setVisibility(View.VISIBLE);
        }
        p1score = 0;
        p2score = 0;
        p1turn = true;
    }

    private void isCupHit() {
        List<ImageView> cups;
        List<Point> cupspos;
        if (p1turn) {
            cups = enemycups;
            cupspos = enemycupspos;
        } else {
            cups = playercups;
            cupspos = playercupspos;
        }
        for (int i = 0; i < cups.size() ; i++) {
            edgecup = i;
            if (cups.get(i).getVisibility() == View.INVISIBLE) {
                continue;
            }
            //float tempx = cups.get(i).getX();
            //float tempy = cups.get(i).getY();
            float tempx = cupspos.get(i).x;
            float tempy = cupspos.get(i).y;
            int tempwidth = cups.get(i).getWidth();
            int tempheight = cups.get(i).getHeight();
            int ballwidth = ball.getWidth();
            int ballheight = ball.getHeight();
            System.out.println("Bollens bredd : " + ball.getWidth());

            if(ballx + (ballwidth / 2) >= tempx + (tempwidth / 4)
                    && ballx + (ballwidth / 2) <= tempx + (tempwidth*0.75)
                    && bally + (ballheight / 2) >= tempy - (tempheight / 4)
                    && bally + (ballheight / 2) <= tempy + (tempheight*0.5)) {

                ballOnEdge();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cupIsHit(cups.get(edgecup), edgecup);
                        mediaPlayer.start();
                        return;
                    }
                }, 3500);

                //cupIsHit(cups.get(i), i);
                //mediaPlayer.start();

            }
        }
        changePlayer();
    }

    private void cupIsHit(ImageView imageView, int i) {
        imageView.setImageResource(R.drawable.cupwithball);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(R.drawable.emptycup);
                if (p1turn) {
                    rankingUpdate();
                }
            }
        }, 1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<ImageView> cups;
                int score;
                if (p1turn) {
                    cups = enemycups;
                    p1score++;
                    score = p1score;
                } else {
                    cups = playercups;
                    p2score++;
                    score = p2score;
                }
                cups.get(i).setVisibility(View.INVISIBLE);
                //cups.remove(i);
                //cupspos.remove(i);
                //if(cups.isEmpty()) {
                if (score == 6) {
                    reset();
                } else {
                    changePlayer();
                }
            }
        }, 3000);
    }

    @SuppressLint("ResourceType")
    private void changePlayer() {
        String player;
        if (!p1turn) {
            player = p1name;
        } else {
            player = "Player 2";
        }
        Toast.makeText(getApplicationContext(), player + "'s turn!", Toast.LENGTH_SHORT)
                .show();
        for (int i = 0; i < 6; i ++) {
            switchCupPos(enemycups.get(i), playercups.get(i), i, i);
        }
        /*switchCupPos(enemycups.get(0), playercups.get(3), 0, 3);
        switchCupPos(enemycups.get(1), playercups.get(4), 1, 4);
        switchCupPos(enemycups.get(2), playercups.get(5), 2, 5);
        switchCupPos(enemycups.get(3), playercups.get(2), 3, 2);
        switchCupPos(enemycups.get(4), playercups.get(1), 4, 1);
        switchCupPos(enemycups.get(5), playercups.get(0), 5, 0);*/
        p1turn = !p1turn;
    }

    private void switchCupPos(ImageView cup1, ImageView cup2, int idx1, int idx2) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int x1 = enemycupspos.get(idx1).x;
            int y1 = enemycupspos.get(idx1).y;
            int x2 = playercupspos.get(idx2).x;
            int y2 = playercupspos.get(idx2).y;
            Path path = new Path();
            path.moveTo(x1, y1);
            path.lineTo(x2, y2);
            Path path2 = new Path();
            path2.moveTo(x2, y2);
            path2.lineTo(x1, y1);
            ObjectAnimator animator = ObjectAnimator.ofFloat(cup1, View.X, View.Y, path);
            animator.setDuration(2000);
            animator.start();
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(cup2, View.X, View.Y, path2);
            animator2.setDuration(2000);
            animator2.start();
            enemycupspos.get(idx1).set(x2, y2);
            playercupspos.get(idx2).set(x1, y1);
        }
    }

    private void rankingUpdate() {
        ranking++;
        rankingText.setText(String.valueOf(ranking));
        totalHits =+ ranking;
        getIntent().putExtra("y", totalHits);
        //Toast.makeText(getApplicationContext(), "Total Hits: " + getIntent().getIntExtra("y", 0), Toast.LENGTH_SHORT)
                //.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (enemycupspos.isEmpty() && playercupspos.isEmpty()) {
            addEnemyCupsPos();
            addPlayerCupsPos();
        }
        /*if(counter == 3){
            onPause();
            ballOnEdge();
            onResume();
        }
         */
        counter++;
        float maxFlingVelocity    = ViewConfiguration.get(getApplicationContext()).getScaledMaximumFlingVelocity();
        float velocityPercentX    = velocityX / maxFlingVelocity;          // the percent is a value in the range of (0, 1]
        float normalizedVelocityX = velocityPercentX * PIXELS_PER_SECOND;  // where PIXELS_PER_SECOND is a device-independent measurement
        float velocityPercentY    = velocityY / maxFlingVelocity;          // the percent is a value in the range of (0, 1]
        float normalizedVelocityY = velocityPercentY * PIXELS_PER_SECOND;  // where PIXELS_PER_SECOND is a device-independent measurement
        int ballwidth = ball.getWidth();
        int ballheight = ball.getHeight();
        System.out.println("fling");
        Log.v("speed", "velocityX: " + String.valueOf(velocityX) + " velocityY: " + String.valueOf(velocityY));
        Path path = new Path();
        //startpunkt för animation
        path.moveTo(e1.getX() - (ballwidth / 2), e1.getY() - ballheight - 150);
        //slutpunkt för animation
        path.lineTo(e2.getX() + (normalizedVelocityX) - (ballwidth / 2), e2.getY() + (normalizedVelocityY) - ballheight - 150);
        //path.arcTo(0f, 0f, 1000f, 1000f, 270f, -180f, true);
        ObjectAnimator animator = ObjectAnimator.ofFloat(ball, View.X, View.Y, path);
        animator.setDuration(2000);
        animator.start();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ballx = ball.getX();
                bally = ball.getY();
                isCupHit();            }
        }, 2000);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (this.mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    public void ballOnEdge(){
        Intent intent = new Intent(this, OnEdgeActivity.class);
        startActivity(intent);
    }

    public void drink(){
        Intent intent = new Intent(this, DrinkingActivity.class);
        startActivity(intent);
    }

}