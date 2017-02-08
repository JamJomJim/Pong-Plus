package objects;

import com.badlogic.gdx.physics.box2d.World;
import com.powerpong.game.PowerPong;

public class AIPaddle extends Paddle {
    private Ball ball;

    public AIPaddle(String textureName, float x, float y, World world, Ball ball) {
        super(textureName, x, y, world);
        this.ball = ball;
    }
    public void update() {
        float finalDestination = calcFinalDestination(ball.getX(), ball.getY(), ball.getBody().getLinearVelocity().x, ball.getBody().getLinearVelocity().y);
        destination.set(finalDestination, this.getY());
        super.update();
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
}
//test