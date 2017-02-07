package objects;

import com.badlogic.gdx.physics.box2d.World;
import com.powerpong.game.PowerPong;

/**
 * Created by Nick on 2/5/2017.
 */
public class AIPaddle extends Paddle {
    private Ball ball;
    private float offset = 0;

    public AIPaddle(String textureName, float x, float y, World world, Ball ball) {
        super(textureName, x, y, world);
        this.ball = ball;
    }

    /*
    Set the paddle's destination.x to the x coord of the ball, and do the necessary physics stuff to get there
     */
    public void update() {
        float timeToPaddle = (this.getY() - ball.getY()) / ball.getBody().getLinearVelocity().y;
        float finalDestination = ball.getX() + ball.getBody().getLinearVelocity().x * timeToPaddle;
        int bounces = (int)(finalDestination / (PowerPong.NATIVE_WIDTH / PowerPong.PPM));
        if(bounces % 2 == 1)
            finalDestination = PowerPong.NATIVE_WIDTH / PowerPong.PPM - (finalDestination % (PowerPong.NATIVE_WIDTH / PowerPong.PPM));
        else
            finalDestination = finalDestination % (PowerPong.NATIVE_WIDTH / PowerPong.PPM);
        destination.set(finalDestination, destination.y);

        //System.out.println(finalDestination);
        super.update();
    }
    public void resetOffset(){
        offset = 0; //(float)(Math.floor(Math.random() * 351) - 175) / PowerPong.PPM ;
        //System.out.println(offset);
    }
}
