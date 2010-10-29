package de.bananer.nowitzki;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnTouchListener;

class GameEngine extends Thread implements OnTouchListener {

    /*
     * State-tracking constants
     */
    public static final int STATE_READY = 1;
    public static final int STATE_RUNNING = 2;
    public static final int STATE_GAMEOVER = 3;
    private int state;
    /** Handle to the surface manager object we interact with */
    private SurfaceHolder mSurfaceHolder;
    protected int screenHeight;
    protected int screenWidth;
    // to stop thread when app is hidden
    private boolean mRun;
    // timing
    private long mLastTime;
    private Playground playground;
    // screen message handler
    private final Handler mHandler;
    private final Context context;

    // user input handling
    protected DragHandler dragHandler;

    public GameEngine(SurfaceHolder surfaceHolder, Context context, Handler handler) {
        // get handles to some important objects
        this.mHandler = handler;
        this.context = context;
        this.mSurfaceHolder = surfaceHolder;

        //holder.addCallback(this);
    }


    public boolean onTouch(View view, MotionEvent event) {
        this.playground.setBall(this.dragHandler.onTouch(event, playground.getBall()));
        return true;
    }


    public void doDraw(Canvas canvas) {
        // clear / draw Background
        canvas.drawARGB(255, 50, 50, 50);



        // draw Ball
        Paint p = new Paint();
        p.setAntiAlias(true);

        if (dragHandler.isDragging()) {
            p.setARGB(255, 100, 180, 240);
            canvas.drawCircle(this.dragHandler.getLastPoint().x, this.dragHandler.getLastPoint().y, this.playground.getBallRadius(), p);

        } else {
            p.setARGB(255, 240, 180, 100);
            canvas.drawCircle(this.playground.getBall().x, this.playground.getBall().y, this.playground.getBallRadius(), p);
        }

        //Log.d("Engine", "Drawing Ball: "+this.playground.getBallPos().x + " - "+ this.playground.getBallPos().y );

        canvas.drawCircle(this.playground.getBall().x, this.playground.getBall().y, this.playground.getBallRadius(), p);

    }

    /**
     * Used to signal the thread whether it should be running or not.
     * Passing true allows the thread to run; passing false will shut it
     * down if it's already running. Calling start() after this was most
     * recently called with false will result in an immediate shutdown.
     *
     * @param b true to run, false to shut down
     */
    public void setRunning(boolean b) {
        mRun = b;
    }

    private void updatePlayground() {
        long now = System.currentTimeMillis();
        // Do nothing if mLastTime is in the future.
        // This allows the game-start to delay the start of the physics
        // by 100ms or whatever.
        if (mLastTime > now) {
            return;
        }
        double elapsed = (now - mLastTime) / 1000.0;
        mLastTime = now;
        try {
            playground.step(elapsed);
        } catch (GameOverException gameOver) {
            //this.state = STATE_GAMEOVER;
        }
    }

    @Override
    public void run() {
        this.mLastTime = System.currentTimeMillis();
        while (mRun) {
            Canvas c = null;
            try {
                c = mSurfaceHolder.lockCanvas(null);
                synchronized (mSurfaceHolder) {

                    if (this.state == STATE_RUNNING && !dragHandler.isDragging()) {
                        this.updatePlayground();
                    }

                    doDraw(c);

                }
            } catch (Exception ex) {
                Log.e("GameEngine", "Exception in run()", ex);
            } finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent state
                if (c != null) {
                    mSurfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
        Log.d("thread", "run() -> " + mRun);
    }

    public void startGame() {
        this.state = STATE_RUNNING;
    }

    public void setSurfaceSize(int width, int height) {
        Log.w("engine", "Surface Size changed: " + width + "x" + height);
        // synchronized to make sure these all change atomically
        synchronized (mSurfaceHolder) {
            this.screenWidth = width;
            this.screenHeight = height;
            
            this.playground = new Playground(this.screenWidth, this.screenHeight);
            this.dragHandler = new DragHandler();
            
            // don't forget to resize the background image
            //mBackgroundImage = mBackgroundImage.createScaledBitmap(
            //        mBackgroundImage, width, height, true);
        }
    }

    public void pause() {
        Log.w("Thread", "pause");
    }
}
