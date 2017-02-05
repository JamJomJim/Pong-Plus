package objects;

import com.badlogic.gdx.physics.box2d.World;

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
        if (ball.getBody().getLinearVelocity().y > 0)
            destination.set(ball.getX(), destination.y);
        super.update();
    }
}
