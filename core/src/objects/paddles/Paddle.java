package objects.paddles;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.powerpong.game.Options;
import com.powerpong.game.PowerPong;

public class Paddle extends InputAdapter {

    protected NinePatchDrawable ninePatch;
    protected Body body;

    protected float movespeed;
    protected Vector2 destination;
    
    protected Options options;

    public Paddle() {
    }

    public Paddle(String textureName, float x, float y, World world, Options options) {
        ninePatch = new NinePatchDrawable(new NinePatch(new Texture(textureName)));
        this.options = options;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(x, y); //note that the origin for bodys is at the center; so the player will initially be centered at the passed x and y coordinates

        body = world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(options.paddleWidth / 2 / PowerPong.PPM, ninePatch.getMinHeight() / 2 / PowerPong.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f; //set friction to 0 so that moving into a wall while falling will not slow the player
        fixtureDef.restitution = 1f; //1 restitution so that the ball rebounds perfectly

        body.createFixture(fixtureDef);
        shape.dispose();
        destination = new Vector2(x, y);
    }

    //Apply the necessary physics stuff to the paddle to get it to move towards it's destination.
    public void update(float dt) {
        //if the distance between the paddle and it's destination is less than or equal to the distance it can travel in a single world.step(),
        //then just move the paddle to the destination. This prevents jitter caused by overshooting the destination repeatedly.
        float distance = Math.abs(destination.x - body.getPosition().x);
        if (distance > 0 && distance <= movespeed * dt)
            body.setTransform(destination.x, body.getPosition().y, 0);
        if (destination.x < body.getPosition().x)
            body.setLinearVelocity(-movespeed, 0);
        else if (destination.x > body.getPosition().x)
            body.setLinearVelocity(movespeed, 0);
        else
            body.setLinearVelocity(0, 0);
    }

    //Draw the paddle, centered at body.x and body.y
    public void draw(SpriteBatch sb) {
        ninePatch.draw(sb,
                body.getPosition().x - options.paddleWidth / 2 / PowerPong.PPM,
                body.getPosition().y - ninePatch.getMinHeight() / 2 / PowerPong.PPM,
                options.paddleWidth / PowerPong.PPM,
                ninePatch.getMinHeight() / PowerPong.PPM);
    }

    //note that these methods return the x and y coords of the center of the body, in box2d coords/measurements
    public float getX() {
        return body.getPosition().x;
    }

    public float getY() {
        return body.getPosition().y;
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getDest() {
        return destination;
    }

    public NinePatchDrawable getNinePatch() {
        return ninePatch;
    }

    public void dispose() {

    }
}
