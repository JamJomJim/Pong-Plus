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
        //float timeToPaddle = (this.getY() - ball.getY()) / ball.getBody().getLinearVelocity().y;
        float finalDestination = calcFinalDestination(ball.getX(), ball.getY(), ball.getBody().getLinearVelocity().x, ball.getBody().getLinearVelocity().y);
        destination.set(finalDestination, this.getY());
        //System.out.println(finalDestination);
        super.update();
    }

    public float calcFinalDestination(float xPos, float yPos, float xVel, float yVel){
        float timeToPaddle = (this.getY() - this.getTexture().getHeight() / PowerPong.PPM - yPos) / yVel; //30 for the height of the paddle. Should reference the Paddles texture instead.
        float finalDestination = xPos + xVel * timeToPaddle;
      //  System.out.println("x:" +xPos + "y:" + yPos + "xVel:" + xVel + "yVel:" + yVel); //Testing Purposes
        if (finalDestination < -PowerPong.NATIVE_WIDTH / 2 / PowerPong.PPM) {
            yPos = yPos + ((-PowerPong.NATIVE_WIDTH / 2 / PowerPong.PPM - xPos + ball.getTexture().getWidth() / PowerPong.PPM) / xVel) * yVel; //75 is the width of the ball. Need to take that into account or it thinks it hits the wall later than it actually does.
            xPos = -PowerPong.NATIVE_WIDTH / 2 / PowerPong.PPM;
            xVel = -xVel;
            return calcFinalDestination(xPos, yPos, xVel, yVel);
        }
        else if (finalDestination > PowerPong.NATIVE_WIDTH / 2 / PowerPong.PPM) {
            yPos = yPos + ((PowerPong.NATIVE_WIDTH / 2 / PowerPong.PPM - xPos - ball.getTexture().getWidth() / PowerPong.PPM) / xVel) * yVel; //Same as above. Should reference the Balls texture instead.
            xPos = PowerPong.NATIVE_WIDTH / 2 / PowerPong.PPM;
            xVel = -xVel;
            return calcFinalDestination(xPos, yPos, xVel, yVel);
        }
        else return finalDestination;
    }
}
