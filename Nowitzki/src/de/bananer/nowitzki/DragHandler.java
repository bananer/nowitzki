package de.bananer.nowitzki;

import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Philip Frank <ich@philipfrank.de>
 */
public class DragHandler {

    class DragEvent{
        float x;
        float y;
        long time;

        public DragEvent(float x, float y, long time) {
            this.x = x;
            this.y = y;
            this.time = time;
        }
        
    }

    private boolean dragging;

    private List<DragEvent> events;

    public boolean isDragging() {
        return this.dragging;
    }

    public PointF getLastPoint() {
        DragEvent e = events.get(events.size()-1);
        return new PointF(e.x, e.y);
    }

    private void addEvent(MotionEvent ev) {
        this.events.add(new DragEvent(ev.getX(), ev.getY(), ev.getEventTime()));

    }

    public Ball onTouch(MotionEvent ev, Ball ball) {

        Ball retBall = ball;

        
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
           this.events = new ArrayList<DragEvent>();
           this.addEvent(ev);

           Log.d("GameEngine", "User is holding Ball..."+debugEvent(ev));
           this.dragging = true;
        }
        else if (ev.getAction() == MotionEvent.ACTION_MOVE) {

            this.addEvent(ev);
        }
        else if (ev.getAction() == MotionEvent.ACTION_UP) {
            Log.d("GameEngine", "User released Ball!"+debugEvent(ev));

            if(this.events.size() > 1) {
                
                DragEvent firstEvent = this.events.get(0);
                long time = ev.getEventTime() - firstEvent.time;

                float speedX = 1000* (ev.getX() - firstEvent.x) / time;
                float speedY = 1000* (ev.getY() - firstEvent.y) / time;


                Log.d("engine", "movement: " + (ev.getX() - firstEvent.x) + " / " + (ev.getY() - firstEvent.y) + " in " + (time/1000.0f));
                
                retBall = new Ball(ev.getX(), ev.getY(), speedX, speedY);
                this.dragging = false;
            }

        }
        else if (ev.getAction() == MotionEvent.ACTION_CANCEL) {
            Log.d("GameEngine", "User cancelled holding Ball");
            this.dragging = false;

        }

        return retBall;
    }


    private String debugEvent(MotionEvent event) {
        return "[x: "+event.getX()+" | x: "+event.getY()+" | time: "+event.getEventTime()+" ]";
    }
}
