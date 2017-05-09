package bochhuang2.scm.pumpit;


import android.graphics.PointF;
import android.util.Log;

import static bochhuang2.scm.pumpit.PumpIt.score1;
import static bochhuang2.scm.pumpit.PumpIt.score2;
import static bochhuang2.scm.pumpit.PumpIt.screenH;
import static bochhuang2.scm.pumpit.PumpIt.screenW;

/**
 * Created by bochhuang2 on 2/16/2017.
 */

public class PlayBall extends Ball {

    boolean isCollied = false;

    boolean isBallvsBall = false;

    boolean isDestroy = false;

    static int id = 0;

    int myId;

    boolean isAddList = false;

    public PlayBall(float x, float y, int rad){
        pos.x = x;
        pos.y = y;

        dir = new PointF(1,0);
        speed = 20;

        r = rad;

        id ++;
        myId = id;
    }

    public boolean isDestroy(){
        return isDestroy;
    }

    public int getMyId()
    {
        return myId;
    }

    public boolean isAddList(){

        return isAddList;
    }

    public void setAddList(boolean b){
        isAddList = b;

    }

    void setDir(float x, float y){
        dir.x = x;
        dir.y = y;
    }

    public void Move()
    {

        if(!(dir.x == 0 && dir.y == 0)) {

            float ratio = 1/(float)Math.sqrt((dir.x)*(dir.x) + (dir.y)*(dir.y));

            pos.x += dir.x * ratio * speed;
            pos.y += dir.y * ratio * speed;



        }

        if (speed >= 0){
            speed -= 0.1f;
            if(speed<0)
            {
                speed = 0;

                dir.x = 0;
                dir.y = 0;
            }
        }

        //Log.d("collided","bool: " + isCollied);
        //Log.d("collided","pos: " + pos);

    }
    float normalize(PointF p)
    {
        float ratio;
        ratio = (float)Math.sqrt((p.x)*(p.x) + (p.y) * (p.y));

        if (p.x == 0 && p.y ==0)
            ratio = 1;

        return ratio;
    }

    public void playBallVsPlayBall(PlayBall pb){
        if ((pb.getPos().x - pos.x) * (pb.getPos().x - pos.x) + (pb.getPos().y - pos.y) * (pb.getPos().y - pos.y) < (r + pb.radius()) * (r + pb.radius()) && !isBallvsBall)
        // bouncing -> distance between play ball and ball is smaller than ball.r + play.r
        {

            isBallvsBall = true;
            PointF c_NormalX, c_NormalY;     // collision normal

            if (pb.getPos().x >= pos.x) {
                c_NormalX = new PointF(pb.getPos().x - pos.x, pb.getPos().y - pos.y);
                c_NormalY = new PointF(pb.getPos().y - pos.y, pos.x - pb.getPos().x);
            } else {
                c_NormalX = new PointF(pos.x - pb.getPos().x, pos.y - pb.getPos().y);
                c_NormalY = new PointF(pos.y - pb.getPos().y, pb.getPos().x - pos.x);
            }

            // unit normal X
            //PointF uNX = new PointF(c_NormalX.x/((float)(Math.sqrt((c_NormalX.x)*(c_NormalX.x) + (c_NormalX.y)*(c_NormalX.y)))),c_NormalX.y/((float)(Math.sqrt((c_NormalX.x)*(c_NormalX.x) + (c_NormalX.y)*(c_NormalX.y)))));
            PointF uNX = new PointF(c_NormalX.x / (normalize(c_NormalX)), c_NormalX.y / ((normalize(c_NormalX))));

            // unit normal Y
            //PointF uNY = new PointF(c_NormalY.x/((float)(Math.sqrt((c_NormalY.x)*(c_NormalY.x) + (c_NormalY.y)*(c_NormalY.y)))),c_NormalY.y/((float)(Math.sqrt((c_NormalY.x)*(c_NormalY.x) + (c_NormalY.y)*(c_NormalY.y)))));
            PointF uNY = new PointF(c_NormalY.x / (normalize(c_NormalY)), c_NormalY.y / ((normalize(c_NormalY))));

            PointF thisDirX = new PointF((dir.x * uNX.x / (normalize(dir)) + dir.y * uNX.y / (normalize(dir))) * uNX.x, (dir.x * uNX.x / (normalize(dir)) + dir.y * uNX.y / (normalize(dir))) * uNX.y);
            PointF thisDirY = new PointF((dir.x * uNY.x / (normalize(dir)) + dir.y * uNY.y / (normalize(dir))) * uNY.x, (dir.x * uNY.x / (normalize(dir)) + dir.y * uNY.y) * uNY.y / (normalize(dir)));

            PointF pbDirX = new PointF((pb.getDir().x * uNX.x / (normalize(pb.getDir())) + pb.getDir().y / (normalize(pb.getDir())) * uNX.y) * uNX.x, (pb.getDir().x / (normalize(pb.getDir())) * uNX.x + pb.getDir().y / (normalize(pb.getDir())) * uNX.y) * uNX.y);

            PointF newDirX = new PointF(((r - pb.radius())*thisDirX.x * speed + 2*pb.radius()*pbDirX.x * pb.getSpeed()) / (r + pb.radius()), ((r - pb.radius())*thisDirX.y * speed + 2*pb.radius()*pbDirX.y * pb.getSpeed()) / (r + pb.radius()));

            PointF newRealDir = new PointF(newDirX.x + thisDirY.x, newDirX.y + thisDirY.y);

            dir.x = newRealDir.x / normalize(newRealDir);
            dir.y = newRealDir.y / normalize(newRealDir);

            speed = newRealDir.x / dir.x;
            Log.d("dd","ball" + myId + ": " + dir);
        /*
        float AngleThis, AnglePb;   // angle of direction and normal

        AngleThis = (float) Math.acos(
                (dir.x * c_Normal.x + dir.y * c_Normal.y)
                /( Math.sqrt((dir.x)*(dir.x) + (dir.y)*(dir.y)) * Math.sqrt((c_Normal.x)*(c_Normal.x) + (c_Normal.y)*(c_Normal.y)) ));

        AnglePb = (float) Math.acos(
                (pb.getDir().x * c_Normal.x + pb.getDir().y * c_Normal.y)
                        /( Math.sqrt((pb.getDir().x)*(pb.getDir().x) + (pb.getDir().y)*(pb.getDir().y)) * Math.sqrt((c_Normal.x)*(c_Normal.x) + (c_Normal.y)*(c_Normal.y)) ));

        float thisDirX = dir*/
        }
        else if ((pb.getPos().x - pos.x) * (pb.getPos().x - pos.x) + (pb.getPos().y - pos.y) * (pb.getPos().y - pos.y) >= (r + pb.radius()) * (r + pb.radius()))
        {

            isBallvsBall = false;
        }
    }


    public void Bouncing(Ball b){

        float b1X = b.getPos().x;
        float b1Y = b.getPos().y;





            if ((b1X - pos.x) * (b1X - pos.x) + (b1Y - pos.y) * (b1Y - pos.y) < (r + b.radius()) * (r + b.radius()) && !isCollied)
            // bouncing -> distance between play ball and ball is smaller than ball.r + play.r
            {
                isCollied = true;


                if(b.getDir().x == 0 && b.getDir().y == 0)
                {

                    dir.x = -dir.x;
                    dir.y = -dir.y;


                } else {

                    if (dir.x * b.getDir().x <= 0)
                        dir.x += b.getDir().x;

                    if(dir.x * b.getDir().x > 0 && dir.x < b.getDir().x)
                        dir.x = b.getDir().x;

                    if (dir.y * b.getDir().y <= 0)
                        dir.y += b.getDir().y;

                    if(dir.y * b.getDir().y > 0 && dir.y < b.getDir().y)
                        dir.y = b.getDir().y;

                    if(b.getSpeed()>speed)
                    speed = b.getSpeed();

                    speed *= 0.9;

                }

                // put this outside the ball

                PointF vect = new PointF(pos.x - b.getPos().x, pos.y - b.getPos().y);

                float rat = (float) Math.sqrt((vect.x)*(vect.x) + (vect.y)*(vect.y))/(r+b.radius()) * 0.99f;

                PointF newP =new PointF (vect.x/rat + b.getPos().x,  vect.y/ rat + b.getPos().y);

                pos.x = newP.x;
                pos.y = newP.y;



                //Log.d("str","new p: " + newP + " rat: " + rat);


            }
            else if ((b1X - pos.x) * (b1X - pos.x) + (b1Y - pos.y) * (b1Y - pos.y) >= (r + b.radius()) * (r + b.radius())) {
                isCollied = false;
            }



        if ((b1X - pos.x) * (b1X - pos.x) + (b1Y - pos.y) * (b1Y - pos.y) < (r + b.radius()) * (r + b.radius()))
        {
            PointF vect = new PointF(pos.x - b.getPos().x, pos.y - b.getPos().y);

            float rat = (float) Math.sqrt((vect.x)*(vect.x) + (vect.y)*(vect.y))/(r+b.radius());

            PointF newP =new PointF (vect.x/(rat*0.9f) + b.getPos().x,  vect.y/(rat*0.9f) + b.getPos().y);

            pos.x = newP.x;
            pos.y = newP.y;


        }


    }




    public void ChangeDir()
    {
        if (pos.x + r> screenW)
        {
            dir.x = -dir.x;
            pos.x = screenW - r;
        }
        if (pos.x < r)
        {
            pos.x = r;
            dir.x = -dir.x;
        }
        if (pos.y + r > screenH &&(pos.x -r < 395 || pos.x +r > 1034)  )
        {
            dir.y = -dir.y;
            pos.y = screenH - r;
        }
        if (pos.y < r &&(pos.x -r < 395 || pos.x +r > 1034))
        {
            dir.y = -dir.y;
            pos.y = r;
        }

        if (pos.y - r > screenH && !isDestroy){
            isDestroy = true;

            // p2 get score

            score2 ++;

            Log.d("score","score1: " + score1 + "score2: " + score2);
        }
        if(  pos.y + r < 0 && !isDestroy){
            isDestroy = true;

            // p1 get score
            score1 ++;

            Log.d("score","score1: " + score1 + " score2: " + score2);
        }
    }




}
