package objects;

import com.badlogic.gdx.physics.box2d.World;
import com.powerpong.game.PowerPong;

public class AIPaddle extends Paddle {
    private float OFFSET_MAX = 160; //Width of paddle is currently 320 so an offset above 160 would cause the AI to miss sometimes.
    private float AI_MOVESPEED; //movespeed is a separate variable from NORM_MS so that paddle speed can be changed by powerups etc.

    public enum Diff {
        EASY, MEDIUM, HARD, SKYNET, IMPOSSIBLE
    }
    private Diff diff;
    private Ball ball;

    private float offset;

    public AIPaddle(String textureName, float x, float y, World world, Ball ball, Diff difficulty) {
        super(textureName, x, y, world);
        this.ball = ball;
        this.diff = difficulty;
        switch(diff){
            case EASY:
                OFFSET_MAX = 0;
                AI_MOVESPEED = 2;
                break;
            case MEDIUM:
                OFFSET_MAX = 80;
                AI_MOVESPEED = 3;
                break;
            case HARD:
                OFFSET_MAX = 160;
                AI_MOVESPEED = 4;
                break;
            case SKYNET:
                OFFSET_MAX = 160;
                AI_MOVESPEED = 6;
                break;
            case IMPOSSIBLE:
                OFFSET_MAX = 160;
                AI_MOVESPEED = 15;
                break;
        }
        movespeed = AI_MOVESPEED;
    }
    public void update(float dt) {
        float finalDestination = calcFinalDestination(ball.getX(), ball.getY(), ball.getBody().getLinearVelocity().x, ball.getBody().getLinearVelocity().y);
        destination.set(finalDestination + offset, this.getY());
        super.update(dt);
    }

    public float calcFinalDestination(float xPos, float yPos, float xVel, float yVel){
        float timeToPaddle = (this.getY() - this.getTexture().getHeight() / PowerPong.PPM - yPos) / yVel;
        float finalDestination = xPos + xVel * timeToPaddle;
        if (finalDestination < -PowerPong.NATIVE_WIDTH / 2 / PowerPong.PPM) {
            yPos = yPos + ((-PowerPong.NATIVE_WIDTH / 2 / PowerPong.PPM - xPos + ball.getTexture().getWidth() / PowerPong.PPM) / xVel) * yVel;
            xPos = -PowerPong.NATIVE_WIDTH / 2 / PowerPong.PPM;
            xVel = -xVel;
            return calcFinalDestination(xPos, yPos, xVel, yVel);
        }
        else if (finalDestination > PowerPong.NATIVE_WIDTH / 2 / PowerPong.PPM) {
            yPos = yPos + ((PowerPong.NATIVE_WIDTH / 2 / PowerPong.PPM - xPos - ball.getTexture().getWidth() / PowerPong.PPM) / xVel) * yVel;
            xPos = PowerPong.NATIVE_WIDTH / 2 / PowerPong.PPM;
            xVel = -xVel;
            return calcFinalDestination(xPos, yPos, xVel, yVel);
        }
        else return finalDestination;
    }
    public void randomizeOffset(){
        this.offset = (float)Math.floor(Math.random() * (OFFSET_MAX * 2 + 1) - OFFSET_MAX) / PowerPong.PPM;
    }
}
