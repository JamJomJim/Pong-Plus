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
        destination.set(ball.getX(), destination.y);
        //if the distance between the paddle and it's destination is less than or equal to the distance it can travel in a single world.step(),
        //then just move the paddle to the destination. This prevents jitter caused by overshooting the destination repeatedly.
        float distance = Math.abs(destination.x - body.getPosition().x);
        if (distance <= movespeed * .016 && distance > 0)
            body.setTransform(destination.x, body.getPosition().y, 0);
        if (destination.x < body.getPosition().x)
            body.setLinearVelocity(-movespeed, 0);
        else if (destination.x > body.getPosition().x)
            body.setLinearVelocity(movespeed, 0);
        else
            body.setLinearVelocity(0, 0);
    }
}
