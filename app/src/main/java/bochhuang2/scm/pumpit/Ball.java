package bochhuang2.scm.pumpit;

import android.graphics.PointF;

import static bochhuang2.scm.pumpit.PumpIt.screenH;

/**
 * Created by 黄波铖 on 2017/2/11.
 */

public class Ball {

    // constructor
    public Ball(float x, float y)
    {
        pos.x = x;
        pos.y = y;

        if(y > screenH/2){

            id = 0;
        }
        else if(y<= screenH/2 - r*2){

            id = 1;
        }
        else {
            pos.y = screenH/2 - r*2;
        }

    }

    public Ball(){}             // default constructor

    // private variable
    protected PointF pos = new PointF();  // position of player ball
    protected int r = 100;                // radius of Player Ball
    protected PointF dir = new PointF();
    protected float speed = 0;

    int id;
    int touchId;

    public void setTouchId(int i){
        touchId = i;
    }
    public int getTouchId(){
        return touchId;
    }
    // Member function
    public void setPos(float x, float y)
    {
        pos.x = x;
        pos.y = y;
    }

    public PointF getPos()
    {
        return pos;
    }

    public int radius(){
        return r;
    }

    public void setDir(PointF c, PointF p) // c -> current position; p -> previous position
    {
        dir = new PointF(c.x - p.x, c.y-p.y);
    }

    public void setSpeed(float s)
    {
        speed = s/10;
        //if(speed >10)
         //   speed = 10;
    }

    public PointF getDir()
    {
        return dir;
    }

    public float getSpeed()
    {
        return speed;
    }

    public boolean MoveRestrict()
    {
        if(id == 0){

            if(pos.y < screenH*0.55f){
                pos.y = screenH*0.55f + 1;

                return false;
            }
            else return true;
        }
        else if(id == 1){

            if(pos.y > screenH*0.55f - r*2){
                pos.y = screenH*0.55f - r*2 - 1;
                return false;
            }
            else return true;
        }
        else return true;
    }
}
