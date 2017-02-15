package com.powerpong.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import objects.paddles.AIPaddle;
import objects.Ball;
import objects.paddles.Paddle;
import objects.powerups.Powerup;


public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {
	private Paddle paddleOne, paddleTwo;
	private Vector2 ballVel;

    public ContactListener(Paddle paddleOne, Paddle paddleTwo) {
    	this.paddleOne = paddleOne;
    	this.paddleTwo = paddleTwo;
	}

	@Override
	public void beginContact(Contact contact) {
		Object objectA = contact.getFixtureA().getBody().getUserData(); //These might be redundant
		Object objectB = contact.getFixtureB().getBody().getUserData();
		Body bodyA = contact.getFixtureA().getBody();
		Body bodyB = contact.getFixtureB().getBody();

		//if the collision is between a paddle and the ball, rebound the ball appropriately
		if (objectA instanceof Paddle && objectB instanceof Ball) {
			((Ball) objectB).paddleRebound((Paddle )objectA);
			ballVel = bodyB.getLinearVelocity();
		} else if (objectB instanceof Paddle && objectA instanceof Ball) {
			((Ball) objectA).paddleRebound((Paddle )objectB);
            ballVel = bodyA.getLinearVelocity();
		}

		//Sets separate offsets for the AI whenever a different paddle is hit.
		if ((objectA instanceof Ball && objectB == paddleOne || objectA == paddleOne && objectB instanceof Ball) &&
                paddleTwo instanceof AIPaddle) {
            ((AIPaddle) paddleTwo).randomizeOffset();
        } else if ((objectA instanceof Ball && objectB == paddleTwo || objectA == paddleTwo && objectB instanceof Ball) &&
                paddleOne instanceof AIPaddle) {
            ((AIPaddle) paddleOne).randomizeOffset();
        }

		//Removes powerups when they're hit.
        if (objectA instanceof Ball && objectB instanceof Powerup) {
            ((Powerup )objectB).setDead(true);
        }
        else if (objectB instanceof Ball & objectA instanceof  Powerup) {
            ((Powerup )objectA).setDead(true);
        }
	}

	@Override
	public void endContact (Contact contact){
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve (Contact contact, Manifold oldManifold){
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve (Contact contact, ContactImpulse impulse) {
        Object objectA = contact.getFixtureA().getBody().getUserData(); //These might be redundant
        Object objectB = contact.getFixtureB().getBody().getUserData();
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();
        if (objectA instanceof Paddle && objectB instanceof Ball) {
            bodyB.setLinearVelocity(ballVel);
        } else if (objectB instanceof Paddle && objectA instanceof Ball) {
            bodyA.setLinearVelocity(ballVel);
        }
	}
}
