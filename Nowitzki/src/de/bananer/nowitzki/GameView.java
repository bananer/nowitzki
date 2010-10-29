package de.bananer.nowitzki;

import android.R.color;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

/**
 *
 * @author Philip Frank <ich@philipfrank.de>
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback  {

    /** Handle to the application context, used to e.g. fetch Drawables. */
    private Context mContext;

    private GameEngine engine;

    
    public Thread getEngine() {
        return this.engine;
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        // create thread only; it's started in surfaceCreated()
        this.engine = new GameEngine(holder, context, new Handler() {
            @Override
            public void handleMessage(Message m) {
            }
        });


        this.setClickable(true);

        this.setOnTouchListener(this.engine);
        
        setFocusable(true); // make sure we get key events
        Log.d("view", "GameView created!");
    }

        /**
     * Standard override to get key-press events.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        return false;
    }

    /**
     * Standard override for key-up. We actually care about these, so we can
     * turn off the engine or stop rotating.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) {
        return false;
    }

    /**
     * Standard window-focus override. Notice focus lost so we can pause on
     * focus lost. e.g. user switches to take a call.
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus) engine.pause();
    }


    /* Callback invoked when the surface dimensions change. */
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        engine.setSurfaceSize(width, height);
    }

    /*
     * Callback invoked when the Surface has been created and is ready to be
     * used.
     */
    public void surfaceCreated(SurfaceHolder holder) {
        // start the thread here so that we don't busy-wait in run()
        // waiting for the surface to be created
        Log.d("view", "surfaceCreated() -> Starting engine thread!");
        engine.setRunning(true);
        engine.setSurfaceSize(holder.getSurfaceFrame().width(), holder.getSurfaceFrame().height());

        engine.startGame();
        engine.start();
    }

    /*
     * Callback invoked when the Surface has been destroyed and must no longer
     * be touched. WARNING: after this method returns, the Surface/Canvas must
     * never be touched again!
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
        boolean retry = true;
        engine.setRunning(false);
        while (retry) {
            try {
                engine.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

}
