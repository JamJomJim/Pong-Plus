package objects;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.powerpong.game.PowerPong;

/**
 * Created by Nick on 2/3/2017.
 */
public class Paddle extends InputAdapter {

    protected Texture texture;
    protected Body body;

    protected float movespeed; //movespeed is a separate variable from NORM_MS so that paddle speed can be changed by powerups etc.
    protected Vector2 destination;

    public Paddle() {
    }

    public Paddle(String textureName, float x, float y, World world) {
        this.texture = new Texture(textureName);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(x, y); //note that the origin for bodys is at the center; so the player will initially be centered at the passed x and y coordinates

        body = world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(texture.getWidth() / 2 / PowerPong.PPM, texture.getHeight() / 2 / PowerPong.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f; //set friction to 0 so that moving into a wall while falling will not slow the player
        fixtureDef.restitution = 1f; //1 restitution so that the ball rebounds perfectly

        body.createFixture(fixtureDef);
        shape.dispose();
        destination = new Vector2(x, y);
    }

    /*
    Apply the necessary physics stuff to the paddle to get it to move towards it's destination.
     */
    public void update(float dt) {
        //if the distance between the paddle and it's destination is less than or equal to the distance it can travel in a single world.step(),
        //then just move the paddle to the destination. This prevents jitter caused by overshooting the destination repeatedly.
        float distance = Math.abs(destination.x - body.getPosition().x);
        if (distance <= movespeed * dt && distance > 0)
            body.setTransform(destination.x, body.getPosition().y, 0);
        if (destination.x < body.getPosition().x)
            body.setLinearVelocity(-movespeed, 0);
        else if (destination.x > body.getPosition().x)
            body.setLinearVelocity(movespeed, 0);
        else
            body.setLinearVelocity(0, 0);
    }

    /*
    Draw the paddle, centered at body.x and body.y
     */
    public void draw(SpriteBatch sb) {
        sb.draw(texture,
                body.getPosition().x - texture.getWidth() / 2 / PowerPong.PPM,
                body.getPosition().y - texture.getHeight() / 2 / PowerPong.PPM,
                texture.getWidth() / PowerPong.PPM,
                texture.getHeight() / PowerPong.PPM);
    }

    //note that these methods return the x and y coords of the center of the body, in box2d coords/measurements
    public float getX() {
        return body.getPosition().x;
    }

    public float getY() {
        return body.getPosition().y;
    }

    public Vector2 getDest() {
        return destination;
    }

    public Texture getTexture() {
        return texture;

    }
    public void dispose() {
        texture.dispose();
    }
}
