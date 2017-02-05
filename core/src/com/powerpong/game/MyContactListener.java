package com.powerpong.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import objects.Ball;
import objects.Paddle;
public class MyContactListener implements ContactListener {
	private float ANGLE_MULTIPLIER = 4; //Increase the angle of the balls bounce

	public MyContactListener() {
	}

	@Override
	public void beginContact(Contact contact) {
        Object objectA = contact.getFixtureA().getBody().getUserData(); //These might be redundant
		Object objectB = contact.getFixtureB().getBody().getUserData();
		Body bodyA = contact.getFixtureA().getBody();
		Body bodyB = contact.getFixtureB().getBody();
        float speedAdded = 2; //Increases speed of the ball every bounce in order to make the gameplay speed up

        if (objectA instanceof Paddle && objectB instanceof Ball){
		    float posDiff = bodyB.getPosition().x - bodyA.getPosition().x; //Checks the relative positions of the ball to the paddle
            bodyB.applyLinearImpulse(new Vector2(posDiff * ANGLE_MULTIPLIER, -speedAdded), bodyB.getLocalCenter(), true); //speedAdded increases the speed when it is negative. I think it applies the impulse before it "bounces".
		}

		else if (objectB instanceof Paddle && objectA instanceof Ball){
            float posDiff = bodyA.getPosition().x - bodyB.getPosition().x; //Checks the relative positions of the ball to the paddle
            bodyB.applyLinearImpulse(new Vector2(posDiff * ANGLE_MULTIPLIER, -speedAdded), bodyA.getLocalCenter(), true);
		}
		System.out.println(bodyB.getLinearVelocity());
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
