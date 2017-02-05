package objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.powerpong.game.PowerPong;
import states.PlayState;

//TODO: something is up with the ball, it seems to jitter while moving, more so when moving faster
public class Ball {
    protected Texture texture;
    protected Body body;
    protected PlayState state;
    private float initialSpeed;

    public Ball(String textureName, float x, float y, float initialDirection, float initialSpeed, World world, PlayState state) {
        this.texture = new Texture(textureName);
        this.state = state;
        this.initialSpeed = initialSpeed;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(x, y); //note that the origin for bodys is at the center; so the player will initially be centered at the passed x and y coordinates

        body = world.createBody(bodyDef);
        body.setUserData(this);

        CircleShape shape = new CircleShape();
        shape.setRadius(texture.getWidth() / 2 / PowerPong.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 1f;

        body.createFixture(fixtureDef);
        shape.dispose();

        //Applies initial force to start ball moving.
        applyForce(initialSpeed, initialDirection);
    }

    public void update() {
        float angle;
        if (body.getPosition().y < -PowerPong.NATIVE_HEIGHT / 2 / PowerPong.PPM) {
            state.score("top");
            angle = -90;
        }
        else if (body.getPosition().y > PowerPong.NATIVE_HEIGHT / 2 / PowerPong.PPM) {
            state.score("bot");
            angle = 90;
        }
        else return;
        body.setTransform(0, 0, 0);
        body.setLinearVelocity(0, 0);
        applyForce(initialSpeed, angle);
    }

    public void draw(SpriteBatch sb) {
        sb.draw(texture,
                body.getPosition().x - texture.getWidth() / 2 / PowerPong.PPM,
                body.getPosition().y - texture.getHeight() / 2 / PowerPong.PPM,
                texture.getWidth() / PowerPong.PPM,
                texture.getHeight() / PowerPong.PPM);
    }

    //pass angle as degrees
    public void applyForce(float magnitude, float angle) {
        angle = (float)(angle / 180 * Math.PI);
        body.applyForceToCenter((float)Math.cos(angle) * magnitude, (float)Math.sin(angle) * magnitude, true );
    }

    public float getX() {
        return body.getPosition().x;
    }

    public float getY() {
        return body.getPosition().y;
    }

    public Body getBody() {
        return body;
    }

    public void dispose() {
        texture.dispose();
    }
}

