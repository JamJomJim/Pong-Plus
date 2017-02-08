package objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.powerpong.game.PowerPong;
import screens.PlayScreen;

public class Ball {
    private float ANGLE_MULTIPLIER = 6; //Increase the angle of the balls bounce
    private float SPEED_ADDED = 1; //Increases speed of the ball every bounce in order to make the gameplay speed up

    protected Texture texture;
    protected Body body;
    protected PlayScreen screen;
    private float initialSpeed;

    public Ball(String textureName, float x, float y, float initialDirection, float initialSpeed, World world, PlayScreen screen) {
        this.texture = new Texture(textureName);
        this.screen = screen;
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

        body.setLinearVelocity((float)Math.cos(initialDirection) * initialSpeed,
                (float)Math.sin(initialDirection) * initialSpeed);
    }

    public void update() {
        float angle;
        if (body.getPosition().y < -PowerPong.NATIVE_HEIGHT / 2 / PowerPong.PPM) {
            screen.score("top");
            angle = -1;
        }
        else if (body.getPosition().y > PowerPong.NATIVE_HEIGHT / 2 / PowerPong.PPM) {
            screen.score("bot");
            angle = 1;
        }
        else return;
        body.setTransform(0, 0, 0);
        body.setLinearVelocity(0, initialSpeed * angle);
    }

    public void draw(SpriteBatch sb) {
        sb.draw(texture,
                body.getPosition().x - texture.getWidth() / 2 / PowerPong.PPM,
                body.getPosition().y - texture.getHeight() / 2 / PowerPong.PPM,
                texture.getWidth() / PowerPong.PPM,
                texture.getHeight() / PowerPong.PPM);
    }

    //NOTE: If the screen goes black when the ball hits far from the paddle center, its because posDiff * ANGLE_MULTIPLIER is too big.
    //Because if posDiff * ANGLE_MULTIPLIER is greater than curSpeed squared, then it will be trying to take the sqrt of a negative number.
    //Changing PPM will affect this, since a smaller PPM will mean the posDiff will be larger.
    public void paddleRebound(Body bodyB) {
        float posDiff = body.getPosition().x - bodyB.getPosition().x; //Checks the relative positions of the ball to the paddle
        float curSpeed = (float)Math.sqrt(Math.pow(body.getLinearVelocity().x, 2) + Math.pow(body.getLinearVelocity().y, 2)); //calculate the ball's current speed
        curSpeed += SPEED_ADDED; //increase the speed to speed up the game over time
        //sets the ball's linear velocity; the x component depends on the position difference, and the y component is the overall speed minus the new x component
        //together, the x and y component have a magnitude equal to that of curSpeed
        //TODO: ball is hit at more dramatic angles at slower speeds due to how the new linearvelocity is calculated
        body.setLinearVelocity(posDiff * ANGLE_MULTIPLIER, (float)Math.sqrt(Math.pow(curSpeed, 2) - Math.pow(posDiff * ANGLE_MULTIPLIER, 2)));
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

    public Texture getTexture() {
        return texture;
    }

    public void dispose() {
        texture.dispose();
    }
}


