package objects;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.vandykeewens.pongplus.Options;
import com.vandykeewens.pongplus.PongPlus;
import objects.paddles.Paddle;

public class Ball {

    protected Body body;
    private boolean paused;
    private Vector2 pausedVel; //for remembering what the ball's vel was before pausing, so it can resume

    private ShapeRenderer sr;

    private Options options;

    public Ball(float x, float y, float initialDirection, World world, Options options) {
        pausedVel = new Vector2();
        paused = false;
        this.options = options;

        sr = new ShapeRenderer();
        sr.setColor(Color.WHITE);


        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);
        body.setUserData(this);

        CircleShape shape = new CircleShape();
        shape.setRadius(options.ballSize / 2 / PongPlus.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 1f;

        body.createFixture(fixtureDef);
        shape.dispose();

        body.setLinearVelocity((float) Math.cos(initialDirection) * options.ballInitialSpeed,
                (float) Math.sin(initialDirection) * options.ballInitialSpeed);
    }

    public void draw(Camera cam) {
        sr.setProjectionMatrix(cam.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.circle(body.getPosition().x, body.getPosition().y, options.ballSize / 2 / PongPlus.PPM, (int) options.ballSize);
        sr.end();
    }

    //Changing PPM will affect this, since a smaller PPM will mean the posDiff will be larger.
    public void paddleRebound(Paddle paddle) {
        Body bodyB = paddle.getBody();
        float posDiff = body.getPosition().x - bodyB.getPosition().x; //Checks the relative positions of the ball to the paddle
        int direction = Math.abs(body.getPosition().y) < bodyB.getPosition().y ? -1 : 1; //which direction the ball should travel after rebounding, based on it's position relative to the paddle
        float reboundAngle = posDiff * 300 / options.paddleWidth * options.ballAngleMultiplier; //* 300 / paddleWidth normalizes it for larger paddles
        float curSpeed = (float) Math.sqrt(Math.pow(body.getLinearVelocity().x, 2) + Math.pow(body.getLinearVelocity().y, 2)); //calculate the ball's current speed
        curSpeed += options.ballSpeedIncrease; //increase the speed to speed up the game over time
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
        body.setLinearVelocity(0, options.ballInitialSpeed * direction);
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
        sr.dispose();
    }
}


