package com.example.digipong;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
    private TextView rankingTextRed;
    private TextView rankingTextBlue;
    private List<ImageView> enemycups = new ArrayList<>();
    private List<ImageView> playercups = new ArrayList<>();
    private List<Point> enemycupspos = new ArrayList<>();
    private List<Point> playercupspos = new ArrayList<>();
    private ImageView ball;
    private GestureDetectorCompat mDetector;
    private float ballx;
    private float bally;
    private int ranking; // Borde vi byta namn till pointsRed?
    private int pointsBlue;
    private int totalHits;
    private Vibrator vibrator;
    final Handler handler = new Handler();
    public static final float PIXELS_PER_SECOND = 10000;
    private MediaPlayer mediaPlayer, swoosh;
    private boolean p1turn = true;
    private int p1score = 0;
    private int p2score = 0;
    private String p1name;
    public int edgecup;
    public boolean playerchange = true;
    private float currentballx;
    private float currentbally;
    private ActivityResultLauncher<Intent> mGetContent;
    private ActivityResultLauncher<Intent> mGetContent2;
    private int throwLimit = 0;

    //Sizes of cups
    int normalPlayerSize;
    int normalEnemySize;

    private boolean isCheating = false; //sätt denna till true om ni vill göra spelet lättare vid testning

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mediaPlayer = MediaPlayer.create(this, R.raw.onhit);
        swoosh = MediaPlayer.create(this, R.raw.swoosh);
        ball = findViewById(R.id.pingpongball);
        addEnemyCups();
        addPlayerCups();
        for (ImageView cup : enemycups) {
            cup.setAlpha(1f);
        }
        for (ImageView cup : playercups) {
            cup.setAlpha(0.5f);
        }

        ranking = 0;
        rankingTextRed = findViewById(R.id.scoreRed);
        rankingTextBlue = findViewById(R.id.scoreBlue);
        totalHits = getIntent().getIntExtra("y", 0);
        getIntent().removeExtra("y");
        p1name = getIntent().getStringExtra("x");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        }

        mDetector = new GestureDetectorCompat(this,this);
        mDetector.setOnDoubleTapListener(this);

        Toast.makeText(getApplicationContext(), "x & y val" + ball.getX() + ball.getY(), Toast.LENGTH_SHORT)
                .show();

        mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            boolean res = data.getBooleanExtra("result", true);
                            onEdgeFinish(res);
                        }
                    }
                });
        mGetContent2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            List<ImageView> cups;
                            if (p1turn) {
                                cups = enemycups;
                            } else {
                                cups = playercups;
                            }
                            drinkFinish(cups.get(edgecup), edgecup);
                        }
                    }
                });
        /*
         xBig = playercups.get(0).getLayoutParams().width;
         yBig = playercups.get(0).getLayoutParams().height;
         xSmall = enemycups.get(0).getLayoutParams().width;
         ySmall = enemycups.get(0).getLayoutParams().height;
         */
        normalEnemySize = enemycups.get(0).getLayoutParams().height;
        normalPlayerSize = playercups.get(0).getLayoutParams().height;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
            cup.setImageResource(R.drawable.blue_filledcup);
            cup.setVisibility(View.VISIBLE);
            cup.setAlpha(1f);
        }
        this.ranking = 0;
        for (ImageView cup : playercups) {
            cup.setImageResource(R.drawable.filledcup);
            cup.setVisibility(View.VISIBLE);
            cup.setAlpha(0.5f);
        }
        p1score = 0;
        p2score = 0;
        p1turn = true;
        Path path = new Path();
        path.moveTo(currentballx, currentbally);
        path.lineTo(ballx, bally);
        ObjectAnimator animator = ObjectAnimator.ofFloat(ball, View.X, View.Y, path);
        animator.setDuration(1000);
        animator.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
            if (cups.get(i).getVisibility() == View.INVISIBLE) {
                continue;
            }
            float tempx = cupspos.get(i).x;
            float tempy = cupspos.get(i).y;
            int tempwidth = cups.get(i).getWidth();
            int tempheight = cups.get(i).getHeight();
            int ballwidth = ball.getWidth();
            int ballheight = ball.getHeight();
            System.out.println("Bollens bredd : " + ball.getWidth());

            if (currentballx + (ballwidth / 2) >= tempx + (tempwidth / 4)
                    && currentballx + (ballwidth / 2) <= tempx + (tempwidth * 0.75)
                    && currentbally + (ballheight / 2) >= tempy - (tempheight / 4)
                    && currentbally + (ballheight / 2) <= tempy + (tempheight * 0.5)) {

                edgecup = i;
                ballOnEdge();
                return;
            }

        }
        changePlayer();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onEdgeFinish(boolean result) {
            if (result) {
                List<ImageView> cups;
                if (p1turn) {
                    cups = enemycups;
                } else {
                    cups = playercups;
                }
                cupIsHit(cups.get(edgecup), edgecup);
                mediaPlayer.start();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drink();
                    }
                }, 2000);

            } else {
                changePlayer();
            }
    }

    private void cupIsHit(ImageView imageView, int i) {
        int cupWithBall;
        int emptyCup;
        if(p1turn){
            cupWithBall = R.drawable.blue_cupwithball;
            emptyCup = R.drawable.blue_emptycup;
        } else{
            cupWithBall = R.drawable.cupwithball;
            emptyCup = R.drawable.emptycup;
        }

        imageView.setImageResource(cupWithBall);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        }
        /*handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(emptyCup);
            }
        }, 1000);

        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                rankingUpdate();
                cups.get(i).setVisibility(View.INVISIBLE);
                if (score == 6) {
                    reset();
                } else {
                    changePlayer();
                }
            }
        }, 3000);*/

    }

    private void drinkFinish(ImageView imageView, int i) {
        int emptyCup;
        if(p1turn){
            emptyCup = R.drawable.blue_emptycup;
        } else{
            emptyCup = R.drawable.emptycup;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(emptyCup);
            }
        }, 1000);

        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                rankingUpdate();
                cups.get(i).setVisibility(View.INVISIBLE);
                if (score == 6) {
                    reset();
                } else {
                    changePlayer();
                }
            }
        }, 2000);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceType")
    private void changePlayer() {
        playerchange = true;
        List<ImageView> transparentcups;
        List<ImageView> nottransparentcups;
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

            // Change size
            /*
            if(!p1turn){
                enemycups.get(i).getLayoutParams().height = normalEnemySize;
                enemycups.get(i).requestLayout();
                playercups.get(i).getLayoutParams().height = normalPlayerSize;
                playercups.get(i).requestLayout();
            } else {
                enemycups.get(i).getLayoutParams().height = normalEnemySize + 75;
                enemycups.get(i).requestLayout();
                playercups.get(i).getLayoutParams().height = normalPlayerSize - 55;
                playercups.get(i).requestLayout();
            }

             */
        }
        p1turn = !p1turn;
        if (p1turn) {
            transparentcups = playercups;
            nottransparentcups = enemycups;
            rankingTextRed.setAlpha(1f);
            rankingTextBlue.setAlpha(0.5f);
        } else {
            transparentcups = enemycups;
            nottransparentcups = playercups;
            rankingTextRed.setAlpha(0.5f);
            rankingTextBlue.setAlpha(1f);
        }
        for (ImageView cup : transparentcups) {
            cup.setAlpha(0.5f);
        }
        for (ImageView cup : nottransparentcups) {
            cup.setAlpha(1f);
        }
        Path path = new Path();
        path.moveTo(currentballx, currentbally);
        path.lineTo(ballx, bally);
        ObjectAnimator animator = ObjectAnimator.ofFloat(ball, View.X, View.Y, path);
        animator.setDuration(1000);
        animator.start();

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

            int enemyCupSize;
            int playerCupSize;
            if(!p1turn){
                enemyCupSize = normalEnemySize;
                playerCupSize = normalPlayerSize;
            } else {
                enemyCupSize = normalEnemySize + 75;
                playerCupSize = normalPlayerSize - 55;
            }

            ValueAnimator enemyCupSizeAnimation = ValueAnimator.ofInt(cup1.getMeasuredHeight(), enemyCupSize);
            enemyCupSizeAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = cup1.getLayoutParams();
                    layoutParams.height = val;
                    cup1.setLayoutParams(layoutParams);
                }
            });
            enemyCupSizeAnimation.setDuration(2000);
            enemyCupSizeAnimation.start();

            ValueAnimator playerCupSizeAnimation = ValueAnimator.ofInt(cup2.getMeasuredHeight(), playerCupSize);
            playerCupSizeAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = cup2.getLayoutParams();
                    layoutParams.height = val;
                    cup2.setLayoutParams(layoutParams);
                }
            });
            playerCupSizeAnimation.setDuration(2000);
            playerCupSizeAnimation.start();
        }
    }

    private void rankingUpdate() {
        if(p1turn) {
            ranking++;
            rankingTextRed.setText(String.valueOf(ranking));
            totalHits = +ranking;
            getIntent().putExtra("y", totalHits);
            //Toast.makeText(getApplicationContext(), "Total Hits: " + getIntent().getIntExtra("y", 0), Toast.LENGTH_SHORT)
            //.show();
        } else{
            pointsBlue++;
            rankingTextBlue.setText(String.valueOf(p2score));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        swoosh.start();
        if (enemycupspos.isEmpty() && playercupspos.isEmpty()) {
            addEnemyCupsPos();
            addPlayerCupsPos();
            throwLimit = playercups.get(5).getTop();
        }
        if (e2.getY() < throwLimit && !isCheating) {
            return true;
        }
        float maxFlingVelocity    = ViewConfiguration.get(getApplicationContext()).getScaledMaximumFlingVelocity();
        float velocityPercentX    = velocityX / maxFlingVelocity;          // the percent is a value in the range of (0, 1]
        float normalizedVelocityX = velocityPercentX * PIXELS_PER_SECOND;  // where PIXELS_PER_SECOND is a device-independent measurement
        float velocityPercentY    = velocityY / maxFlingVelocity;          // the percent is a value in the range of (0, 1]
        float normalizedVelocityY = velocityPercentY * PIXELS_PER_SECOND;  // where PIXELS_PER_SECOND is a device-independent measurement
        int ballwidth = ball.getWidth();
        int ballheight = ball.getHeight();
        ballx = ball.getX();
        bally = ball.getY();
        currentballx = e2.getX() + (normalizedVelocityX) - (ballwidth / 2);
        currentbally = e2.getY() + (normalizedVelocityY) - ballheight - 150;
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
                isCupHit();
            }
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
        Intent intent = new Intent(this,
                OnEdgeActivity.class);
        intent.putExtra("p1turn", p1turn);
        mGetContent.launch(intent);
    }

    public void drink(){
        Intent intentDrink = new Intent(this,
                DrinkingActivity.class);
        intentDrink.putExtra("p1turn", p1turn);
        mGetContent2.launch(intentDrink);
    }

}