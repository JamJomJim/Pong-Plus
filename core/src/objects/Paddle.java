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
    private static final float NORM_MS = 5; //the paddle's normal/usual movespeed, in meters/second

    protected Texture texture;
    protected Body body;

    protected float movespeed;
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
        shape.setAsBox(texture.getWidth() / 2 / PowerPong.PIXELS_IN_METER, texture.getHeight() / 2 / PowerPong.PIXELS_IN_METER);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f; //set friction to 0 so that moving into a wall while falling will not slow the player
        fixtureDef.restitution = 1f; //1 restitution so that the ball rebounds perfectly

        body.createFixture(fixtureDef);
        shape.dispose();

        movespeed = NORM_MS;
        destination = new Vector2(x, y);
    }

    public void update() {
        if (destination.x < body.getPosition().x)
            body.setLinearVelocity(-movespeed, 0);
        else if (destination.x > body.getPosition().x)
            body.setLinearVelocity(movespeed, 0);
        else
            body.setLinearVelocity(0, 0);
    }

    public void draw(SpriteBatch sb) {
        sb.draw(texture,
                body.getPosition().x - texture.getWidth() / 2 / PowerPong.PIXELS_IN_METER,
                body.getPosition().y - texture.getHeight() / 2 / PowerPong.PIXELS_IN_METER,
                texture.getWidth() / PowerPong.PIXELS_IN_METER,
                texture.getHeight() / PowerPong.PIXELS_IN_METER);
    }

    public float getX() {
        return body.getPosition().x;
    }

    public float getY() {
        return body.getPosition().y;
    }

    public void dispose() {
        texture.dispose();
    }
}
