package objects;

import com.badlogic.gdx.physics.box2d.World;
import com.powerpong.game.PowerPong;

import javax.print.attribute.standard.Destination;

/**
 * Created by Nick on 2/5/2017.
 */
public class AIPaddle extends Paddle {
    private Ball ball;

    public AIPaddle(String textureName, float x, float y, World world, Ball ball) {
        super(textureName, x, y, world);
        this.ball = ball;
    }

    /*
    Set the paddle's destination.x to the x coord of the ball, and do the necessary physics stuff to get there
     */
    public void update() {
        //float timeToPaddle = (this.getY() - ball.getY()) / ball.getBody().getLinearVelocity().y;
        float finalDestination = calcFinalDestination(ball.getX(), ball.getY(), ball.getBody().getLinearVelocity().x, ball.getBody().getLinearVelocity().y);
        destination.set(finalDestination, this.getY());
        //System.out.println(finalDestination);
        super.update();
    }

    public float calcFinalDestination(float xPos, float yPos, float xVel, float yVel){
     //   if(yVel > 0 && yPos < this.getY()) {
            float timeToPaddle = (this.getY() - yPos) / yVel;
            float finalDestination = xPos + xVel * timeToPaddle;
            if (finalDestination < -PowerPong.NATIVE_WIDTH / 2 / PowerPong.PPM) {
                yPos = yPos + ((-PowerPong.NATIVE_WIDTH / 2 / PowerPong.PPM - xPos) / xVel) * yVel;
                xPos = -PowerPong.NATIVE_WIDTH / 2 / PowerPong.PPM;
                xVel = -xVel;
                return calcFinalDestination(xPos, yPos, xVel, yVel);
            } else if (finalDestination > PowerPong.NATIVE_WIDTH / 2 / PowerPong.PPM) {
                yPos = yPos + ((PowerPong.NATIVE_WIDTH / 2 / PowerPong.PPM - xPos) / xVel) * yVel;
                xPos = PowerPong.NATIVE_WIDTH / 2 / PowerPong.PPM;
                xVel = -xVel;
                return calcFinalDestination(xPos, yPos, xVel, yVel);
            } else {
                System.out.println(finalDestination);
                return finalDestination;
            }
      //  }
     //   return 0;
    }
}
