package com.example.digipong;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Point;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class GameActivity extends AppCompatActivity implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
    private ImageView table;
    private ImageView[] enemycups = new ImageView[6];
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ball = findViewById(R.id.pingpongball);
        addEnemyCups();
        ranking = 0;
        //mMainLayout = (RelativeLayout) findViewById(R.layout.activity_game);
        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(this,this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);

        //table = (ImageView) findViewById(R.id.table);

    }

    private void addEnemyCups() {
        enemycups[0] = findViewById(R.id.enemycup1);
        enemycups[1] = findViewById(R.id.enemycup2);
        enemycups[2] = findViewById(R.id.enemycup3);
        enemycups[3] = findViewById(R.id.enemycup4);
        enemycups[4] = findViewById(R.id.enemycup5);
        enemycups[5] = findViewById(R.id.enemycup6);
    }

    private Point getPoint(int id) {
        return new Point(3,4);
    }

    private int getRanking() {
        return ranking;
    }

    private boolean isCupHit() {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        System.out.println("fling");
        Log.v("speed", "velocityX: " + String.valueOf(velocityX) + " velocityY: " + String.valueOf(velocityY));
        Path path = new Path();
        int dx = (int) (e2.getX() - e1.getX());
        int dy = (int) (e2.getY() - e1.getY());
        //startpunkt för animation
        path.moveTo(e1.getX(), e1.getY() - 200);
        //slutpunkt för animation
        path.lineTo(e2.getX() + (velocityX), e2.getY() + (velocityY));
        //path.arcTo(0f, 0f, 1000f, 1000f, 270f, -180f, true);
        ObjectAnimator animator = ObjectAnimator.ofFloat(ball, View.X, View.Y, path);
        animator.setDuration(2000);
        animator.start();
        ballx = ball.getX();
        bally = ball.getY();
        return true;


        //maxTranslationX = mMainLayout.getWidth() - ball.getWidth();
        //maxTranslationY = mMainLayout.getHeight() - ball.getHeight();

        //downEvent : when user puts his finger down on the view
        //moveEvent : when user lifts his finger at the end of the movement
        //float distanceInX = Math.abs(moveEvent.getRawX() - downEvent.getRawX());
        //float distanceInY = Math.abs(moveEvent.getRawY() - downEvent.getRawY());

        //mTvFlingDistance.setText("distanceInX : " + distanceInX + "\n" + "distanceInY : " + distanceInY);

        //if (distanceInX > MIN_DISTANCE_MOVED) {
        //Fling Right/Left
        //    FlingAnimation flingX = new FlingAnimation(ball, DynamicAnimation.TRANSLATION_X);
        //    flingX.setStartVelocity(velocityX)
        //.setMinValue(MIN_TRANSLATION) // minimum translationX property
        //.setMaxValue(maxTranslationX)  // maximum translationX property
        //.setFriction(FRICTION)
        //            .start();
        //} else if (distanceInY > MIN_DISTANCE_MOVED) {
        //Fling Down/Up
        //    FlingAnimation flingY = new FlingAnimation(ball, DynamicAnimation.TRANSLATION_Y);
        //    flingY.setStartVelocity(velocityY)
        //.setMinValue(MIN_TRANSLATION)  // minimum translationY property
        //.setMaxValue(maxTranslationY) // maximum translationY property
        //.setFriction(FRICTION)
        //            .start();
        //}
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


}