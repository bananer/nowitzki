package de.bananer.nowitzki;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

/**
 *
 * @author Philip Frank <ich@philipfrank.de>
 */
public class Ball {
    

    // physics constants
    private static final double G_FORCE = 120.;
    // private static final double BALL_WEIGHT = 0.2d;

    private static double bounceX = 0.90d;
    private static double bounceY = 0.90d;


    // speed
    private double speedX;
    private double speedY;

    // position
    public float x;
    public float y;

    public void applyForce(double forceX, double forceY) {
        speedX += forceX;
        speedY += forceY;
    }

    /**
     * Modifies the Ball according to the time passed
     */
    private void doMove(double secondsPassed) {
        this.x += speedX * secondsPassed;
        this.y += speedY * secondsPassed;
    }

    /**
     * Step function
     */
    public void step(double secondsPassed) {

        // apply gravitation
        this.applyForce(0, G_FORCE*secondsPassed);

        this.doMove(secondsPassed);

        //Log.d("ball", "speed: "+speedX+"/"+speedY);

    }

    /**
     * Bounce functions, called from Playground
     */
    public void bouceX() {
        this.speedX = -1f * this.speedX * Ball.bounceX;
    }
    public void bouceY() {
        this.speedY = -1f * this.speedY * Ball.bounceY;
    }

    public double getSpeedX() {
        return speedX;
    }

    public double getSpeedY() {
        return speedY;
    }


    public Ball(float x, float y, double speedX, double speedY) {

        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
        
    }
}
