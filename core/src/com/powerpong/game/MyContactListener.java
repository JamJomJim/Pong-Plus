package com.powerpong.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {

	public MyContactListener() {
	}
	@Override
	public void beginContact(Contact contact) {
		/*Object userDataA = contact.getFixtureA().getBody().getUserData();
		Object userDataB = contact.getFixtureB().getBody().getUserData();
		//when player and an enemy contact
		if (userDataA instanceof Player && userDataB instanceof Entity)
			((Player)userDataA).reduceHealth(((Entity)userDataB).getDamage());
		else if (userDataB instanceof Player && userDataA instanceof Entity)
			((Player)userDataB).reduceHealth(((Entity)userDataA).getDamage());
			*/
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
