package com.powerpong.game;

import com.badlogic.gdx.physics.box2d.*;
import objects.AIPaddle;
import objects.Ball;
import objects.Paddle;
import objects.PlayerPaddle;
import screens.MenuScreen;

public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {
	private Paddle paddleOne, paddleTwo;
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
			((Ball) objectB).paddleRebound(bodyA);
		} else if (objectB instanceof Paddle && objectA instanceof Ball) {
			((Ball) objectA).paddleRebound(bodyB);
		}
		//Sets separate offsets for the AI whenever a different paddle is hit.
		if(objectA instanceof Ball && objectB == paddleOne || objectA == paddleOne && objectB instanceof Ball) {
			if (paddleTwo instanceof AIPaddle)((AIPaddle) paddleTwo).randomizeOffset();
		} else if (objectA instanceof Ball && objectB == paddleTwo || objectA == paddleTwo && objectB instanceof Ball) {
			if (paddleOne instanceof AIPaddle)((AIPaddle) paddleOne).randomizeOffset();
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
	public void postSolve (Contact contact, ContactImpulse impulse){
		// TODO Auto-generated method stub

	}
}
