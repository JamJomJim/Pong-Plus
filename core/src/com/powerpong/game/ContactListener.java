package com.powerpong.game;

import com.badlogic.gdx.physics.box2d.*;
import objects.AIPaddle;
import objects.Ball;
import objects.Paddle;
import objects.PlayerPaddle;
import screens.MenuScreen;

public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {

    public ContactListener() {
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
		//kind of a bad way to check this, tbh idk what is going on here kinda or what purpose it serves
		if (MenuScreen.mode == MenuScreen.Mode.CLASSIC) {
			if (objectA instanceof PlayerPaddle && objectB instanceof Ball) {
				((AIPaddle) objectA).randomizeOffset();
			} else if (objectB instanceof PlayerPaddle && objectA instanceof Ball) {
				((AIPaddle) objectB).randomizeOffset();
			}
		}
		//This is temporary. Need to change once the AI battle is on the menu screen.
		else {
			if (objectA instanceof AIPaddle && objectB instanceof Ball) {
				((AIPaddle) objectA).randomizeOffset();
			} else if (objectB instanceof AIPaddle && objectA instanceof Ball) {
				((AIPaddle) objectB).randomizeOffset();
			}
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
