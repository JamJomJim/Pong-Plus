package objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.powerpong.game.PowerPong;
import objects.paddles.Paddle;
import screens.PlayScreen;

public class Ball {
    private float ANGLE_MULTIPLIER = 60; //Increase the angle of the balls bounce
    private float SPEED_ADDED = 1.5f; //Increases speed of the ball every bounce in order to make the gameplay speed up

    protected Texture texture;
    protected Body body;
    private float initialSpeed;
    private boolean paused;
    private Vector2 pausedVel; //for remembering what the ball's vel was before pausing, so it can resume

    public Ball(String textureName, float x, float y, float initialDirection, float initialSpeed, World world) {
        this.texture = new Texture(textureName);
        this.initialSpeed = initialSpeed;
        pausedVel = new Vector2();
        paused = false;

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

    public void draw(SpriteBatch sb) {
        sb.draw(texture,
                body.getPosition().x - texture.getWidth() / 2 / PowerPong.PPM,
                body.getPosition().y - texture.getHeight() / 2 / PowerPong.PPM,
                texture.getWidth() / PowerPong.PPM,
                texture.getHeight() / PowerPong.PPM);
    }

    //Changing PPM will affect this, since a smaller PPM will mean the posDiff will be larger.
    public void paddleRebound(Paddle paddle) {
        Body bodyB = paddle.getBody();
        float posDiff = body.getPosition().x - bodyB.getPosition().x; //Checks the relative positions of the ball to the paddle
        int direction = Math.abs(body.getPosition().y) < bodyB.getPosition().y ? -1 : 1; //which direction the ball should travel after rebounding, based on it's position relative to the paddle
        float reboundAngle = posDiff * ANGLE_MULTIPLIER;
        float curSpeed = (float)Math.sqrt(Math.pow(body.getLinearVelocity().x, 2) + Math.pow(body.getLinearVelocity().y, 2)); //calculate the ball's current speed
        curSpeed += SPEED_ADDED; //increase the speed to speed up the game over time
        //sets the ball's linear velocity; the x component depends on the position difference, and the y component is the overall speed minus the new x component
        //together, the x and y component have a magnitude equal to that of curSpeed
        body.setLinearVelocity((float) (curSpeed * Math.sin(Math.toRadians(reboundAngle))), (float) (direction * curSpeed * Math.cos(Math.toRadians(reboundAngle))));
    }

    public void pause() {
        //if statement is so that if the ball is already ballPaused, ballVel won't be set to 0, meaning the ball couldn't be "resumed"
        if (body.getLinearVelocity().y != 0)
            pausedVel.set(body.getLinearVelocity());
        body.setLinearVelocity(0, 0);
        paused = true;
    }

    public void resume() {
        body.setLinearVelocity(pausedVel);
        paused = false;
    }

    public boolean isPaused() {
        return paused;
    }

    //reset the ball to 0,0 and start it in direction; -1 is down, 1 is up
    public void reset(int direction) {
        body.setTransform(0, 0, 0);
        body.setLinearVelocity(0, initialSpeed * direction);
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


