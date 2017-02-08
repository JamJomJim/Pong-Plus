package objects;

import com.badlogic.gdx.physics.box2d.World;
import com.powerpong.game.PowerPong;
import screens.PlayScreen;

public class AIPaddle extends Paddle {
    private float maxOffset; //Width of paddle is currently 320 so an offset above 160 would cause the AI to miss sometimes.

    private Ball ball;
    private PlayScreen.AI difficulty;

    private float offset;

    public AIPaddle(String textureName, float x, float y, World world, Ball ball, PlayScreen.AI difficulty) {
        super(textureName, x, y, world);
        this.ball = ball;
        this.difficulty = difficulty;
        switch (difficulty){
            case EASY:
                maxOffset = 0;
                movespeed = 2;
                break;
            case MEDIUM:
                maxOffset = 80;
                movespeed = 3;
                break;
            case HARD:
                maxOffset = 160;
                movespeed = 4;
                break;
            case SKYNET:
                maxOffset = 160;
                movespeed = 6;
                break;
            case IMPOSSIBLE:
                maxOffset = 160;
                movespeed = 15;
                break;
        }
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
        this.offset = (float)Math.floor(Math.random() * (maxOffset * 2 + 1) - maxOffset) / PowerPong.PPM;
    }
}
