package objects.paddles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.pongplus.game.Options;
import com.pongplus.game.PongPlus;
import objects.Ball;

public class AIPaddle extends Paddle {
    private float maxOffset; //options.paddleWidth of paddle is currently 320 so an offset above 160 would cause the AI to miss sometimes.
    private float offset; //current offset for this specific paddle.

    private Ball ball;
    private Vector2 prevVel;

    private ExtendViewport vp;


    public AIPaddle(float x, float y, World world, Ball ball, ExtendViewport vp, Options options) {
        super(x, y, world, options);
        this.prevVel = new Vector2(0, 0);
        this.ball = ball;
        this.vp = vp;
        switch (options.ai) {
            case EASY:
                maxOffset = 0;
                movespeed = 3;
                break;
            case MEDIUM:
                maxOffset = options.paddleWidth / 3;
                movespeed = 5;
                break;
            case HARD:
                maxOffset = options.paddleWidth / 2;
                movespeed = 7;
                break;
            case SKYNET:
                maxOffset = options.paddleWidth / 2;
                movespeed = 20;
                break;
            case CUSTOM:
                maxOffset = options.paddleWidth / 2 * options.aiOffset / 10;
                movespeed = options.aiMovespeed;
        }
        randomizeOffset();
    }

    public void update(float dt) {
        if (!prevVel.isCollinear(ball.getBody().getLinearVelocity()) &&
                ((this.getY() < 0 && ball.getBody().getLinearVelocity().y < 0) ||
                        (this.getY() > 0 && ball.getBody().getLinearVelocity().y > 0)))
            destination.set(calcFinalDestination(
                    ball.getX(),
                    ball.getY(),
                    ball.getBody().getLinearVelocity().x,
                    ball.getBody().getLinearVelocity().y) + offset,
                    this.getY());
        //move towards the destination
        super.update(dt);
    }

    public float calcFinalDestination(float xPos, float yPos, float xVel, float yVel) {
        float timeToPaddle;
        if (this.getY() > yPos)
            timeToPaddle = (this.getY() - this.ninePatch.getMinHeight() / PongPlus.PPM - yPos) / yVel;
        else
            timeToPaddle = (this.getY() + this.ninePatch.getMinHeight() / PongPlus.PPM - yPos) / yVel;
        float finalDestination = xPos + xVel * timeToPaddle;
        if (finalDestination < -vp.getWorldWidth() / 2 + options.ballSize / 2 / PongPlus.PPM) {
            yPos = yPos + ((-vp.getWorldWidth() / 2 - xPos + options.ballSize / PongPlus.PPM) / xVel) * yVel;
            xPos = -vp.getWorldWidth() / 2;
            xVel = -xVel;
            return calcFinalDestination(xPos, yPos, xVel, yVel);
        } else if (finalDestination > vp.getWorldWidth() / 2 - options.ballSize / 2 / PongPlus.PPM) {
            yPos = yPos + ((vp.getWorldWidth() / 2 - xPos - options.ballSize / PongPlus.PPM) / xVel) * yVel;
            xPos = vp.getWorldWidth() / 2;
            xVel = -xVel;
            return calcFinalDestination(xPos, yPos, xVel, yVel);
        }
        prevVel.set(ball.getBody().getLinearVelocity());
        return finalDestination;
    }

    public void setDestination(float x) {
        destination.set(x, destination.y);
    }

    public void randomizeOffset() {
        offset = (float) Math.floor(Math.random() * (maxOffset * 2 + 1) - maxOffset) / PongPlus.PPM;
    }
}
