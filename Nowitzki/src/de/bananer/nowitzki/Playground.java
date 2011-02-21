package de.bananer.nowitzki;

import android.graphics.PointF;
import android.util.Log;

/**
 * Contains the games physics, that the Ball and collision checking
 *
 * @author Philip Frank <ich@philipfrank.de>
 */
public class Playground {

    protected int width;
    protected int height;

    protected int ballRadius;

    private Ball ball;

    public Ball getBall() {
        return ball;
    }


    public int getBallRadius() {
        return ballRadius;
    }
    public void setBall(Ball b) {
        ball = b;
    }


    public void pushBall(double forceX, double forceY) {
        this.ball.applyForce(forceX, forceY);
    }


    private void checkCollision() {
        
        if(this.ball.y >= this.height - this.ballRadius){
            ball.bouceY();

            this.ball.y = this.height - this.ballRadius;

        }

        if((this.ball.x <= this.ballRadius) ) {
            ball.bouceX();
            this.ball.x = this.ballRadius;
        }
        else if(this.ball.x >= this.width - this.ballRadius) {
            this.ball.bouceX();
            this.ball.x = this.width - this.ballRadius;
        }
    }

    public void step(double secondsElapsed) throws GameOverException {

        //Log.d("step", this.toString());

        this.ball.step(secondsElapsed);

        this.checkCollision();

        // ball is on the floor and has small Y speed => Game over.
        if(ball.y == this.height - this.ballRadius
                && Math.abs(this.ball.getSpeedY()) < 0.00001f) {
            //throw new GameOverException();
        }
    }

    public Playground(int width, int height) {
        this.width = width;
        this.height = height;
        this.ballRadius = 15;

        this.ball = new Ball(100,100,50f, 0);
    }

    public String toString() {
        return "Ball ( "+this.ball.x +" / "+this.ball.y + ")";
    }


}
