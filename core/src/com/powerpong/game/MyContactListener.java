package com.powerpong.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import objects.Ball;
import objects.Paddle;
public class MyContactListener implements ContactListener {
	private float ANGLE_MULTIPLIER = 5; //Increase the angle of the balls bounce
	private float SPEED_ADDED = 3; //Increases speed of the ball every bounce in order to make the gameplay speed up

	public MyContactListener() {
	}

	@Override
	public void beginContact(Contact contact) {
        Object objectA = contact.getFixtureA().getBody().getUserData(); //These might be redundant
		Object objectB = contact.getFixtureB().getBody().getUserData();
		Body bodyA = contact.getFixtureA().getBody();
		Body bodyB = contact.getFixtureB().getBody();
		//if the collision is between a paddle and the ball
        if (objectA instanceof Paddle && objectB instanceof Ball) {
		    float posDiff = bodyB.getPosition().x - bodyA.getPosition().x; //Checks the relative positions of the ball to the paddle
			float curSpeed = (float)Math.sqrt(Math.pow(bodyB.getLinearVelocity().x, 2) + Math.pow(bodyB.getLinearVelocity().y, 2)); //calculate the ball's current speed
			System.out.println(curSpeed);
			curSpeed += SPEED_ADDED; //increase the speed to speed up the game over time
			//sets the ball's linear velocity; the x component depends on the position difference, and the y component is the overall speed minus the new x component
			//together, the x and y component have a magnitude equal to that of curSpeed
            bodyB.setLinearVelocity(posDiff * ANGLE_MULTIPLIER, (float)Math.sqrt(Math.pow(curSpeed, 2) - Math.pow(posDiff * ANGLE_MULTIPLIER, 2)));
		}
		else if (objectB instanceof Paddle && objectA instanceof Ball) {
			float posDiff = bodyB.getPosition().x - bodyA.getPosition().x;
			float curSpeed = (float)Math.sqrt(Math.pow(bodyB.getLinearVelocity().x, 2) + Math.pow(bodyB.getLinearVelocity().y, 2));
			System.out.println(curSpeed);
			curSpeed += SPEED_ADDED; //increase the speed to speed up the game over time
			bodyB.setLinearVelocity(posDiff * ANGLE_MULTIPLIER, (float)Math.sqrt(Math.pow(curSpeed, 2) - Math.pow(posDiff * ANGLE_MULTIPLIER, 2)));
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
