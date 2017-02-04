package objects;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.powerpong.game.PowerPong;

/**
 * Created by Nick on 2/3/2017.
 */
public class Paddle extends InputAdapter {
    protected Texture texture;
    protected Body body;

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
        fixtureDef.restitution = 0.0f;

        body.createFixture(fixtureDef);
        shape.dispose();
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
