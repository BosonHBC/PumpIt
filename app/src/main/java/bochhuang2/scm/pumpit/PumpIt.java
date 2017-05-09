package bochhuang2.scm.pumpit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by HUANG Bocheng on 2017/2/11.
 */

public class PumpIt extends View {

    // IMG
    Bitmap bgImg;
    Bitmap p1Win;
    Bitmap p2Win;
    Bitmap startImg;

    double rdSeed = 10;
    Random minRd = new Random();

    // Timer
    Timer t = new Timer();

    // int
    int fps = 60;
    int speedEqualZeroTimes = 0;
    int speedEqualZeroTimes2 = 0;
    static int score1 = 0;
    static int score2 = 0;
    int divLineY; // Line that divide two area

    int runT = 0;
    Random rd = new Random();
    int[] playBallPaint = {
            Color.rgb(rd.nextInt(250),rd.nextInt(250),rd.nextInt(250)),
            Color.rgb(rd.nextInt(250),rd.nextInt(250),rd.nextInt(250)),
            Color.rgb(rd.nextInt(250),rd.nextInt(250),rd.nextInt(250)),
            Color.rgb(rd.nextInt(250),rd.nextInt(250),rd.nextInt(250)),
            Color.rgb(rd.nextInt(250),rd.nextInt(250),rd.nextInt(250)),
            Color.rgb(rd.nextInt(250),rd.nextInt(250),rd.nextInt(250)),
            Color.rgb(rd.nextInt(250),rd.nextInt(250),rd.nextInt(250))};

    int play1InRectTime = 0;
    int play2InRectTime = 0;

    RectF rect1;
    RectF rect2;

    RectF restartR = new RectF(229,1554,1185,1784);

    //RectF startRect = new RectF(150,1514,1264,1744);

    RectF scoreR;
    Paint scorePaint = new Paint();

    // float
    static final int screenW = 1440;
    static final int screenH = 2400;



    PointF[] arrP = {new PointF(0,0), new PointF(1,1)};
    PointF[] arrP2 = {new PointF(0,1), new PointF(1,1)};

    // Paint
    Paint paint1 = new Paint();
    Paint paint2 = new Paint();

    Paint paintBall = new Paint();

    boolean isBallvsBall = false;
    // Balls
    Ball ball_1;
    Ball ball_2;

    List<PlayBall> playBallList = new ArrayList<PlayBall>();

    int addBallTime;

    PlayBall pb1;
    PlayBall pb2;
    PlayBall pb3;
    PlayBall pb4;
    PlayBall pb5;
    PlayBall pb6;
    PlayBall pb7;

    // game state
    //boolean gameStart = false;
    boolean gameOver = false;

    boolean second_pt_down = false;

    // Constructors

    public PumpIt(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init();
    }

    public PumpIt(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init();
    }

    public PumpIt(Context context)
    {
        super(context);

        Init();
    }

    // Main game loop
    private void Init(){

        divLineY = screenH/2;

        ball_1 = new Ball(screenW/2 -100,screenH*2/3 - 200);
        ball_2 = new Ball(screenW/2 -100,screenH/3 - 200);

        // img
        startImg = BitmapFactory.decodeResource(getResources(), R.drawable.title);
        bgImg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        p1Win = BitmapFactory.decodeResource(getResources(), R.drawable.p1win);
        p2Win = BitmapFactory.decodeResource(getResources(), R.drawable.p2win);

        paint1.setColor(Color.rgb(200,100,100));
        paint2.setColor(Color.rgb(100,100,200));
        scorePaint.setColor(Color.rgb(235, 235, 235));


        pb1 = new PlayBall(50,screenH/2,50);

        pb2 = new PlayBall(70, screenH/7,70);
        pb3 = new PlayBall(90, screenH*2/7,90);
        pb4 = new PlayBall(60, screenH*3/7,60);
        pb5 = new PlayBall(60, screenH*4/7,40);
        pb6 = new PlayBall(60, screenH*5/7,50);
        pb7 = new PlayBall(60, screenH*6/7,80);


        playBallList.add(pb1);

        rect1 = new RectF(350,2063,1083,2380);
        rect2 = new RectF(352,10,1085,331);

        addBallTime = 0;
        score2 = 0;
        score1 = 0;

        t_Timer();

    }

    void t_Timer(){

            t.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            // implementing recurring tasks,
                            // Game loop with fps = 30

                            rdSeed = minRd.nextDouble();
                            runT++;
                            if (runT % 2 == 0) {

                                arrP[0].x = ball_1.getPos().x;
                                arrP[0].y = ball_1.getPos().y;
                                ball_1.setDir(arrP[0], arrP[1]);

                                arrP2[0].x = ball_2.getPos().x;
                                arrP2[0].y = ball_2.getPos().y;
                                ball_2.setDir(arrP2[0], arrP2[1]);
                            } else if (runT % 2 != 0) {
                                arrP[1].x = ball_1.getPos().x;
                                arrP[1].y = ball_1.getPos().y;
                                ball_1.setDir(arrP[1], arrP[0]);

                                arrP2[1].x = ball_2.getPos().x;
                                arrP2[1].y = ball_2.getPos().y;
                                ball_2.setDir(arrP2[1], arrP2[0]);
                            }


                            float speed = (float) Math.sqrt(disVsPointF(arrP[0], arrP[1])) * 100 / (1000 / fps);

                            if (speed == 0) {
                                speedEqualZeroTimes++;
                            } else if (speed != 0) {
                                speedEqualZeroTimes = 0;
                            }

                            if (speedEqualZeroTimes == 0)
                                ball_1.setSpeed(speed);

                            if (speedEqualZeroTimes > 5) {
                                ball_1.setSpeed(speed);
                            }

                            ball_1.MoveRestrict();

                            float speed2 = (float) Math.sqrt(disVsPointF(arrP2[0], arrP2[1])) * 100 / (1000 / fps);

                            if (speed2 == 0) {
                                speedEqualZeroTimes2++;
                            } else if (speed2 != 0) {
                                speedEqualZeroTimes2 = 0;
                            }

                            if (speedEqualZeroTimes2 == 0)
                                ball_2.setSpeed(speed2);

                            if (speedEqualZeroTimes2 > 5) {
                                ball_2.setSpeed(speed2);
                            }

                            ball_2.MoveRestrict();


                            // play ball area

                            for (int i = 0; i < playBallList.size(); i++) {
                                if (!playBallList.get(i).isDestroy()) {
                                    playBallList.get(i).Move();
                                    playBallList.get(i).Bouncing(ball_1);
                                    playBallList.get(i).Bouncing(ball_2);
                                    playBallList.get(i).ChangeDir();
                                }
                            }

                            if (playBallList.size() > 1) {

                                for (int i = 0; i < playBallList.size(); i++) {

                                    if (i + 2 < playBallList.size()) {
                                        for (int j = i + 1; j < playBallList.size(); j++) {
                                            playBallVsPlayBall(playBallList.get(i), playBallList.get(j));
                                        }
                                    } else {
                                        playBallVsPlayBall(playBallList.get(i), playBallList.get(i + 1));
                                        break;
                                    }
                                }
                            }
                            // control ball in and out
                            BallInOut();

                            // two second area
                            twoSecond();

                            // GameOver
                            GameOver();

                            postInvalidate(); // refresh drawing

                        }
                    },
                    0, // time in milliseconds before first execution
                    1000 / fps // time in milliseconds between subsequent executions
            );


    }

    // Drawing Function

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.rgb(40,40,40));

        // draw BackGround


        // Size
        int w = this.getWidth();
        int h = this.getHeight();

        // bgImg
        canvas.drawBitmap(bgImg,0,0,null);

        // score
        if (score1>=1) {
            for (int i = 0; i < score1; i++) {


                scoreR = new RectF(441 + i * 100, 1276, 441 + i * 100 + 50, 1326);
                canvas.drawRect(scoreR, scorePaint);
            }
        }

        if (score2>=1) {
            for (int i = 0; i < score2; i++) {


                scoreR = new RectF(441 + i * 100, 1034, 441 + i * 100 + 50, 1084);
                canvas.drawRect(scoreR, scorePaint);
            }
        }

        // P1 Ball

        if (ball_1.MoveRestrict()) {
            canvas.drawCircle(ball_1.getPos().x, ball_1.getPos().y, 110, scorePaint);
            canvas.drawCircle(ball_1.getPos().x, ball_1.getPos().y, ball_1.radius(), paint1);

        }
        else {
            canvas.drawCircle(ball_1.getPos().x, screenH * 0.55f + 1, 110, scorePaint);
            canvas.drawCircle(ball_1.getPos().x, screenH * 0.55f + 1, ball_1.radius(), paint1);

        }
        // P2 Ball

        if (ball_2.MoveRestrict()) {
            canvas.drawCircle(ball_2.getPos().x, ball_2.getPos().y, 110, scorePaint);
            canvas.drawCircle(ball_2.getPos().x, ball_2.getPos().y, ball_2.radius(), paint2);

        }
        else {
            canvas.drawCircle(ball_2.getPos().x, screenH * 0.55f - 2 * ball_2.radius() - 1, 110, scorePaint);
            canvas.drawCircle(ball_2.getPos().x, screenH * 0.55f - 2 * ball_2.radius() - 1, ball_2.radius(), paint2);
        }
        // play ball

        for (int i = 0; i < playBallList.size(); i++)
        {

                paintBall.setColor(playBallPaint[i]);

            canvas.drawCircle(playBallList.get(i).getPos().x,playBallList.get(i).getPos().y, playBallList.get(i).radius(), paintBall);
        }

        if(gameOver) {
            if (score1 > score2)
                canvas.drawBitmap(p1Win,0,0,null);

            if (score2 > score1)
                canvas.drawBitmap(p2Win,0,0,null);
        }


        /*if (!gameStart){
            canvas.drawBitmap(startImg,0,0,null);

        }*/

    }

    public void GameOver(){
        if(score2 >= 6 || score1 >= 6 && !gameOver){
            gameOver = true;
            t.cancel();
            /*for (int i = 0; i<playBallList.size();i++){
                playBallList.remove(i);
            }*/
            playBallList = new ArrayList<PlayBall>();

            addBallTime = 0;
        }
    }

    public void Restart(){
        gameOver = false;

        score1 = 0;
        score2 = 0;

        t = new Timer();
        Init();
    }

    // in 2 second area
    public void twoSecond(){

        if (rect1.contains(ball_1.getPos().x,ball_1.getPos().y)){
            play1InRectTime ++;
            paint1.setColor(Color.rgb(200 + 50* play1InRectTime/(2*fps),100 - 50* play1InRectTime/(2*fps),100- 50* play1InRectTime/(2*fps)));

            if (play1InRectTime >= (2*fps)){

                score2 ++;
                play1InRectTime = 0;
                paint1.setColor(Color.rgb(200, 100,100));
                ball_1.setPos(720, 1400);
            }

        }
        else {
            if (play1InRectTime>0)
                play1InRectTime --;

            paint1.setColor(Color.rgb(200 + 50* play1InRectTime/(2*fps),100 - 50* play1InRectTime/(2*fps),100- 50* play1InRectTime/(2*fps)));

        }

        if (rect2.contains(ball_2.getPos().x,ball_2.getPos().y)){
            play2InRectTime ++;
            paint2.setColor(Color.rgb(100 - 50* play2InRectTime/(2*fps),100 - 50* play2InRectTime/(2*fps),200+ 50* play2InRectTime/(2*fps)));

            if (play2InRectTime >= (2*fps)){

                score1 ++;
                play2InRectTime = 0;
                paint2.setColor(Color.rgb(100, 100,200));
                ball_2.setPos(720, 1000);
            }

        }
        else {
            if (play2InRectTime>0)
                play2InRectTime --;

            paint2.setColor(Color.rgb(100 - 50* play2InRectTime/(2*fps),100 - 50* play2InRectTime/(2*fps),200 + 50* play2InRectTime/(2*fps)));

        }

    }

    // Distance vs two circle

    public float disVsPointF(PointF a, PointF b)
    {
        float d;

        d = (a.x - b.x)*(a.x - b.x) + (a.y - b.y)*(a.y - b.y);

        return d;
    }

    // PlayBall vs playBall


    public void playBallVsPlayBall(PlayBall p1, PlayBall p2){
        if ((p1.getPos().x - p2.getPos().x) * (p1.getPos().x - p2.getPos().x) + (p1.getPos().y - p2.getPos().y) * (p1.getPos().y - p2.getPos().y) < (p2.radius() + p1.radius()) * (p2.radius() + p1.radius()) && !isBallvsBall)
        // bouncing -> distance between play ball and ball is smaller than ball.p2.radius() + play.p2.radius()
        {

            isBallvsBall = true;
            PointF c_NormalX, c_NormalY;     // collision normal

           // if (p1.getPos().x >= p2.getPos().x) {
                c_NormalX = new PointF(p1.getPos().x - p2.getPos().x, p1.getPos().y - p2.getPos().y);
                c_NormalY = new PointF(p1.getPos().y - p2.getPos().y, p1.getPos().x - p2.getPos().x);
            /*} else {
                c_NormalX = new PointF(p1.getPos().x - p2.getPos().x, p1.getPos().y - p2.getPos().y);
                c_NormalY = new PointF(p1.getPos().y - p2.getPos().y, p1.getPos().x - p2.getPos().x);
            }*/

            // unit normal X
            //PointF uNX = new PointF(c_NormalX.x/((float)(Math.sqrt((c_NormalX.x)*(c_NormalX.x) + (c_NormalX.y)*(c_NormalX.y)))),c_NormalX.y/((float)(Math.sqrt((c_NormalX.x)*(c_NormalX.x) + (c_NormalX.y)*(c_NormalX.y)))));
            PointF uNX = new PointF(c_NormalX.x / (normalize(c_NormalX)), c_NormalX.y / ((normalize(c_NormalX))));

            // unit normal Y
            //PointF uNY = new PointF(c_NormalY.x/((float)(Math.sqrt((c_NormalY.x)*(c_NormalY.x) + (c_NormalY.y)*(c_NormalY.y)))),c_NormalY.y/((float)(Math.sqrt((c_NormalY.x)*(c_NormalY.x) + (c_NormalY.y)*(c_NormalY.y)))));
            PointF uNY = new PointF(c_NormalY.x / (normalize(c_NormalY)), c_NormalY.y / ((normalize(c_NormalY))));

            PointF p2DirX = new PointF((p2.getDir().x * uNX.x / (normalize(p2.getDir())) + p2.getDir().y * uNX.y / (normalize(p2.getDir()))) * uNX.x, (p2.getDir().x * uNX.x / (normalize(p2.getDir())) + p2.getDir().y * uNX.y / (normalize(p2.getDir()))) * uNX.y);
            PointF p2DirY = new PointF((p2.getDir().x * uNY.x / (normalize(p2.getDir())) + p2.getDir().y * uNY.y / (normalize(p2.getDir()))) * uNY.x, (p2.getDir().x * uNY.x / (normalize(p2.getDir())) + p2.getDir().y * uNY.y) * uNY.y / (normalize(p2.getDir())));

            PointF p1DirX = new PointF((p1.getDir().x * uNX.x / (normalize(p1.getDir())) + p1.getDir().y * uNX.y / (normalize(p1.getDir())) * uNX.y) * uNX.x, (p1.getDir().x / (normalize(p1.getDir())) * uNX.x + p1.getDir().y / (normalize(p1.getDir())) * uNX.y) * uNX.y);
            PointF p1DirY = new PointF((p1.getDir().x * uNX.x / (normalize(p1.getDir())) + p1.getDir().y * uNY.y / (normalize(p1.getDir())) * uNX.y) * uNX.x, (p1.getDir().x / (normalize(p1.getDir())) * uNX.x + p1.getDir().y / (normalize(p1.getDir())) * uNX.y) * uNX.y);

            PointF newP2DirX = new PointF(((p2.radius() - p1.radius())*p2DirX.x * p2.getSpeed() + 2*p1.radius()*p1DirX.x * p1.getSpeed()) / (p2.radius() + p1.radius()), ((p2.radius() - p1.radius())*p2DirX.y * p2.getSpeed() + 2*p1.radius()*p1DirX.y * p1.getSpeed()) / (p2.radius() + p1.radius()));
            PointF newP1DirX = new PointF(((p1.radius() - p2.radius())*p1DirX.x * p1.getSpeed() + 2*p2.radius()*p2DirX.x * p2.getSpeed()) / (p2.radius() + p1.radius()), ((p1.radius() - p2.radius())*p1DirX.y * p1.getSpeed() + 2*p2.radius()*p2DirX.y * p2.getSpeed()) / (p2.radius() + p1.radius()));

            PointF newP2RealDir = new PointF(newP2DirX.x + p2DirY.x, newP2DirX.y + p2DirY.y);
            PointF newP1RealDir = new PointF(newP1DirX.x + p1DirY.x, newP1DirX.y + p1DirY.y);

            p2.setDir(newP2RealDir.x / normalize(newP2RealDir), newP2RealDir.y / normalize(newP2RealDir));
            p1.setDir(newP1RealDir.x / normalize(newP1RealDir), newP1RealDir.y / normalize(newP1RealDir));

            p2.setSpeed(newP2RealDir.x * 5 / p2.getDir().x);
            p1.setSpeed(newP1RealDir.x* 5 / p1.getDir().x);
            //Log.d("dd","ball" + myId + ": " + p2.getDi())r;

            // out side
            PointF vect = new PointF(p2.getPos().x - p1.getPos().x,p2.getPos().y - p1.getPos().y);

            float rat = (float) Math.sqrt((vect.x)*(vect.x) + (vect.y)*(vect.y))/(p1.radius()+p2.radius()) * 0.99f;

            PointF newP =new PointF (vect.x/(rat*0.9f) + p1.getPos().x,  vect.y/(rat*0.9f) + p1.getPos().y);

            p2.getPos().x = newP.x;
            p2.getPos().y = newP.y;

            PointF vect2 = new PointF(p1.getPos().x - p2.getPos().x,p1.getPos().y - p2.getPos().y);

            float rat2 = (float) Math.sqrt((vect2.x)*(vect2.x) + (vect2.y)*(vect2.y))/(p2.radius()+p1.radius()) * 0.99f;

            PointF newP2 =new PointF (vect2.x/(rat2*0.9f) + p2.getPos().x,  vect2.y/(rat2*0.9f) + p2.getPos().y);

            p1.getPos().x = newP2.x;
            p1.getPos().y = newP2.y;

        }
        else if ((p1.getPos().x - p2.getPos().x) * (p1.getPos().x - p2.getPos().x) + (p1.getPos().y - p2.getPos().y) * (p1.getPos().y - p2.getPos().y) >= (p2.radius() + p1.radius()) * (p2.radius() + p1.radius()))
        {

            isBallvsBall = false;
        }

        if ((p1.getPos().x - p2.getPos().x) * (p1.getPos().x - p2.getPos().x) + (p1.getPos().y - p2.getPos().y) * (p1.getPos().y - p2.getPos().y) < (p2.radius() + p1.radius()) * (p2.radius() + p1.radius()))
        // bouncing -> distance between play ball and ball is smaller than ball.p2.radius() + play.p2.radius()
        {
            // out side
            PointF vect1 = new PointF(p2.getPos().x - p1.getPos().x,p2.getPos().y - p1.getPos().y);

            float rat1 = (float) Math.sqrt((vect1.x)*(vect1.x) + (vect1.y)*(vect1.y))/(p1.radius()+p2.radius()) * 0.99f;

            PointF newP1 =new PointF (vect1.x/(rat1*0.9f) + p1.getPos().x,  vect1.y/(rat1*0.9f) + p1.getPos().y);

            p2.getPos().x = newP1.x;
            p2.getPos().y = newP1.y;

            PointF vect2 = new PointF(p1.getPos().x - p2.getPos().x,p1.getPos().y - p2.getPos().y);

            float rat2 = (float) Math.sqrt((vect2.x)*(vect2.x) + (vect2.y)*(vect2.y))/(p2.radius()+p1.radius()) * 0.99f;

            PointF newP2 =new PointF (vect2.x/(rat2*0.9f) + p2.getPos().x,  vect2.y/(rat2*0.9f) + p2.getPos().y);

            p1.getPos().x = newP2.x;
            p1.getPos().y = newP2.y;



        }
    }

    float normalize(PointF p)
    {
        float ratio;
        ratio = (float)Math.sqrt((p.x)*(p.x) + (p.y) * (p.y));

        if (p.x == 0 && p.y ==0)
            ratio = 1;

        return ratio;
    }

    // ball in and out

    public void BallInOut(){
        // out

        if (playBallList.size()>0) {
            for (int i = 0; i < playBallList.size(); i++) {
                if (playBallList.get(i).isDestroy()){
                    playBallList.get(i).setAddList(false);
                    playBallList.remove(i);
                   // Log.e("size","size: " + playBallList.size());
                }

            }
        }
        // in
        if (playBallList.size()<=0){
            playBallList.add(pb1);

            int newRad = (int)(rdSeed * 50 + 50);

            for (int i =0;i<playBallPaint.length;i++){
                playBallPaint[i] = Color.rgb(minRd.nextInt(250),minRd.nextInt(250),minRd.nextInt(250));
            }

            pb1 = new PlayBall(newRad,screenH/2,newRad);
            Log.e("size","size: " + newRad);
        }

        //Log.d("Size","Size: " + playBallList.size());

        addBallTime++;
        int newRad = (int)(rdSeed * 50 + 50);
        if (addBallTime%840 == 0 && (score2 + score1 >= 3)){
            addBallTime = 0;

            if (!pb2.isAddList()) {
                pb2 = new PlayBall(newRad, screenH/7,newRad);
                playBallList.add(pb2);
                pb2.setAddList(true);
            }

            Log.d("Size"," Add: ");
            if (!pb3.isAddList()) {

                pb3 = new PlayBall(newRad, screenH*2/7,newRad);
                playBallList.add(pb3);
                pb3.setAddList(true);
            }

            if (!pb4.isAddList()) {
                pb4 = new PlayBall(newRad, screenH*3/7,newRad);
                playBallList.add(pb4);
                pb4.setAddList(true);
            }

            if(score2 + score1 >= 8                                                                                                                                                                              ) {
                if (!pb5.isAddList()) {
                    pb5 = new PlayBall(newRad, screenH * 4 / 7, newRad);
                    playBallList.add(pb5);
                    pb5.setAddList(true);
                }


                if (!pb6.isAddList()) {
                    pb6 = new PlayBall(newRad, screenH * 5 / 7, newRad);
                    playBallList.add(pb6);
                    pb6.setAddList(true);
                }


                if (!pb7.isAddList()) {
                    pb7 = new PlayBall(newRad, screenH * 6 / 7, newRad);
                    playBallList.add(pb7);
                    pb7.setAddList(true);
                }
            }

        }


    }

    //  Touch Event

    private float DownX;
    private float DownY;

    float moveX;
    float moveY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                DownX = event.getX();//float DownX
                DownY = event.getY();//float DownY

                for (int i = 0; i < event.getPointerCount(); i++) {
                    int x = (int) event.getX(i);
                    int y = (int) event.getY(i);

                    if (event.getY(i) > screenH*0.55f) {
                        ball_1.setPos(x, y);
                        ball_1.setTouchId(i);
                        Log.d("touch","ball1: " + ball_1.getTouchId());
                    }
                    if(event.getY(i) <= screenH*0.55f - 2*ball_2.radius()){
                        ball_2.setPos(x, y);
                        ball_2.setTouchId(i);
                        Log.d("touch","ball2: " + ball_2.getTouchId());
                    }
                }

                /*if (!gameStart & startRect.contains(DownX,DownY)){
                    gameStart = true;
                    t_Timer();

                }*/

                if (restartR.contains(DownX,DownY) && gameOver){
                    Restart();
                    Log.d("gamer","GameOver: " + gameOver);
                }

                //Log.d("Touch Event", "moveX: " + DownX + " moveX: " + DownY);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:

               /* Log.d("touch2","ball1: " + ball_1.getTouchId());
                for (int i = 0; i < event.getPointerCount(); i++) {

                    }*/
                int a = (int) event.getX(1);
                int b = (int) event.getY(1);
                    if (event.getY(1) > screenH*0.55f) {
                        ball_1.setPos(a, b);
                        ball_1.setTouchId(1);
                        Log.d("touch1","ball1: " + ball_1.getTouchId());
                    }
                    if(event.getY(1) <= screenH*0.55f - 2*ball_2.radius()){
                        ball_2.setPos(a, b);
                        ball_2.setTouchId(1);
                        Log.d("touch2","ball2: " + ball_2.getTouchId());

                    }

                break;
            case MotionEvent.ACTION_POINTER_UP:

                break;
            case MotionEvent.ACTION_MOVE:

                    moveX = event.getX(0);
                    moveY = event.getY(0);


                for (int i = 0; i < event.getPointerCount(); i++) {
                    int x = (int) event.getX(i);
                    int y = (int) event.getY(i);

                    /*if (ball_1.getTouchId()!=9 && event.getY(ball_1.getTouchId()) > screenH*0.55f) {
                        ball_1.setPos(event.getX(ball_1.getTouchId()), event.getY(ball_1.getTouchId()));
                    }
                    if(ball_2.getTouchId()!=9 &&event.getY(ball_2.getTouchId()) <= screenH*0.55f - 2*ball_2.radius()){
                        ball_2.setPos(event.getX(ball_2.getTouchId()), event.getY(ball_2.getTouchId()));
                    }*/

                    if (event.getY(i) > screenH*0.55f) {
                        ball_1.setPos(x, y);

                    }
                    if(event.getY(i) <= screenH*0.55f - 2*ball_2.radius()){
                        ball_2.setPos(x, y);

                    }
                }

                //Log.d("Touch Event", "moveX: " +event.getPointerCount());
                break;
            case MotionEvent.ACTION_UP: {
                /*if (ball_1.getTouchId()!=9 &&event.getY(ball_1.getTouchId()) > screenH*0.55f) {
                    ball_1.setTouchId(9);
                }
                if(ball_2.getTouchId()!=9 &&event.getY(ball_2.getTouchId()) <= screenH*0.55f - 2*ball_2.radius()){
                    ball_2.setPos(event.getX(ball_2.getTouchId()), event.getY(ball_2.getTouchId()));
                    ball_2.setTouchId(9);
                }*/
                break;

            }

        }



        return true;
    }

    public void Destroy(){
        t.cancel();
        playBallList = new ArrayList<PlayBall>();
        addBallTime = 0;
        gameOver = false;

        score1 = 0;
        score2 = 0;
    }

}
