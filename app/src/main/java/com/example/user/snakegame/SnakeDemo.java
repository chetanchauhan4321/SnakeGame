package com.example.user.snakegame;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.Random;


/**
 * Created by user on 12-10-2017.
 */
public class SnakeDemo extends SurfaceView implements Runnable{
    Thread t=null;
    boolean flag =false;
    int swidth,sheight;

    private int[] m_SnakeSx;
    private int[] m_snakeSy;

    private int mSnakeLength;

    private int m_MouseX;
    private int m_MouseY;

    private int m_BlockSize;

    private final int NUM_BLOCK_WIDE=40;

    private  int m_NumBlockHigh;

    enum  direction{UP, RIGHT,DOWN,LEFT};
    direction m_direction=direction.RIGHT;

    private long m_NextFrameTime;


    private int m_Score;
    SurfaceHolder sh;
    Canvas c;
    Paint p;

    Context ct;
    SnakeDemo(Context c,int width, int height)
    {
        super(c);
        ct=c;
        swidth= width;
        sheight=height;

        m_BlockSize=swidth/NUM_BLOCK_WIDE;
        m_NumBlockHigh=sheight/m_BlockSize;

        sh=getHolder();
        p=new Paint();
        m_SnakeSx=new int[200];
        m_snakeSy=new int[200];
        startGame();
    }
    public void startGame()
    {

        Log.d("HELLO","In StartGame");
        mSnakeLength=1;
        m_SnakeSx[0]=NUM_BLOCK_WIDE/2;
        m_snakeSy[0]=m_NumBlockHigh/2;
        spawnMouse();

        m_Score=0;
        m_NextFrameTime=System.currentTimeMillis();

    }
  public void spawnMouse(){
      Log.d("HELLO","In spawnMouse");
      m_MouseX=1+ (int)(Math.random()*(NUM_BLOCK_WIDE-1));
      m_MouseY=1+ (int)(Math.random()*(m_NumBlockHigh-1));

  }
private void eatMouse()
{
    Log.d("HELLO","In eatMouse");
     mSnakeLength++;
    spawnMouse();
    m_Score=m_Score+1;


}
private void moveSnake()
{
    Log.d("HELLO","In moveSnake");
    for (int i=mSnakeLength;i>0;i--)
    {
        m_SnakeSx[i]=m_SnakeSx[i-1];
        m_snakeSy[i]=m_snakeSy[i-1];

    }
    switch (m_direction)
    {
        case UP:
            m_snakeSy[0]--;
            break;
        case RIGHT:
            m_SnakeSx[0]++;
            break;
        case DOWN:
            m_snakeSy[0]++;
            break;
        case LEFT:
            m_SnakeSx[0]--;
            break;
    }


}
@Override
   public void run()
  {
      while (flag)
      {
          Log.d("Hello","In Run");
          if (checkForUpdate())
          {
              updateGame();
              drawGame();
          }
      }
  }

public void pause() {
    flag = false;
    try {
        t.join();
    } catch (InterruptedException ie) {
    }
}

public void resume()
    {
        flag=true;
        t=new Thread(this);
        t.start();
    }

    public boolean checkForUpdate() {
        Log.d("Hello","In check for update");

        // Are we due to update the frame
        if(m_NextFrameTime <= System.currentTimeMillis()){
            // Tenth of a second has passed

            // Setup when the next update will be triggered
            m_NextFrameTime =System.currentTimeMillis() + 100;



            // Return true so that the update and draw
            // functions are executed
            return true;
        }
        return false;
    }
    public void updateGame() {

        Log.d("Hello","In updategame");
        // Did the head of the snake touch the mouse?
        if (m_SnakeSx[0] == m_MouseX && m_snakeSy[0] == m_MouseY) {
            eatMouse();
        }

        moveSnake();

        if (detectDeath()) {
            //start again
            startGame();
        }
    }
    public void drawGame()
    {
        // Prepare to draw
        Log.d("Hello","In draw Game");
        if (sh.getSurface().isValid()) {

            c = sh.lockCanvas();

            // Clear the screen with my favorite color
            c.drawColor(Color.BLACK);

            // Set the color of the paint to draw the snake and mouse with
            p.setColor(Color.GRAY);

            // Choose how big the score will be
            p.setTextSize(50);
            c.drawText("Score:" + m_Score, 10, 30, p);

            //Draw the snake
            for (int i = 0; i < mSnakeLength; i++) {
                c.drawRect(m_SnakeSx[i]* m_BlockSize,
                        (m_snakeSy[i] * m_BlockSize),
                        (m_SnakeSx[i] * m_BlockSize) + m_BlockSize,
                        (m_snakeSy[i] * m_BlockSize) + m_BlockSize,
                        p);
            }

            //draw the mouse
            c.drawRect(m_MouseX * m_BlockSize,
                    (m_MouseY * m_BlockSize),
                    (m_MouseX * m_BlockSize) + m_BlockSize,
                    (m_MouseY * m_BlockSize) + m_BlockSize,
                    p);

            // Draw the whole frame
            sh.unlockCanvasAndPost(c);
        }
    }

    private boolean detectDeath() {
        // Has the snake died?
        Log.d("Hello","In Detect Death");
        boolean dead = false;

        // Hit a wall?
        if (m_SnakeSx[0] == -1) dead = true;
        if (m_SnakeSx[0] >= NUM_BLOCK_WIDE) dead = true;
        if (m_snakeSy[0] == -1) dead = true;
        if (m_snakeSy[0] == m_NumBlockHigh) dead = true;

        // Eaten itself?
        for (int i =mSnakeLength - 1; i > 0; i--) {
            if ((i > 4) && (m_SnakeSx[0] == m_SnakeSx[i]) && (m_snakeSy[0] == m_snakeSy[i])) {
                dead = true;
            }
        }

        return dead;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_UP:
                if (event.getX() >= swidth / 2) {

                    switch(m_direction){
                        case UP:
                            m_direction = direction.RIGHT;
                            break;
                        case RIGHT:
                            m_direction = direction.DOWN;
                            break;
                        case DOWN:
                            m_direction = direction.LEFT;
                            break;
                        case LEFT:
                            m_direction = direction.UP;
                            break;
                    }
                } else {
                    switch(m_direction){
                        case UP:
                            m_direction = direction.LEFT;
                            break;
                        case LEFT:
                            m_direction = direction.DOWN;
                            break;
                        case DOWN:
                            m_direction = direction.RIGHT;
                            break;
                        case RIGHT:
                            m_direction = direction.UP;
                            break;
                    }
                }
        }
        return true;
    }


}
