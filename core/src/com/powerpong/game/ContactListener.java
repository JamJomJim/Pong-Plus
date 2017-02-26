package com.powerpong.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import objects.paddles.AIPaddle;
import objects.Ball;
import objects.paddles.Paddle;
import screens.PlayScreen;


public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {
	private Paddle paddleOne, paddleTwo;
	private Vector2 ballVel;
	private PlayScreen screen;

	private Sound botSound, topSound;
	private float pitch;


    public ContactListener(Paddle paddleOne, Paddle paddleTwo, PlayScreen screen) {
        this.paddleOne = paddleOne;
        this.paddleTwo = paddleTwo;
        this.screen = screen;
        ballVel = new Vector2();

        botSound = Gdx.audio.newSound(Gdx.files.internal("sounds/botboop.wav")); //TODO: dispose this later
        topSound = Gdx.audio.newSound(Gdx.files.internal("sounds/topboop.wav")); //TODO: dispose this later
        pitch = 1f;
	}

	@Override
	public void beginContact(Contact contact) {
		Object objectA = contact.getFixtureA().getBody().getUserData(); //These might be redundant
		Object objectB = contact.getFixtureB().getBody().getUserData();
		Body bodyA = contact.getFixtureA().getBody();
		Body bodyB = contact.getFixtureB().getBody();

		//if the collision is between a paddle and the ball and the ball is on the "inside" of the paddle, rebound the ball appropriately
		if (objectA instanceof Paddle && objectB instanceof Ball &&
                Math.abs(bodyB.getPosition().y) < Math.abs(bodyA.getPosition().y)) {
			((Ball) objectB).paddleRebound((Paddle )objectA);
			ballVel.set(bodyB.getLinearVelocity());
			if (objectA == paddleOne)
			    botSound.play();
			else
			    topSound.play();
		} else if (objectB instanceof Paddle && objectA instanceof Ball &&
                Math.abs(bodyA.getPosition().y) < Math.abs(bodyB.getPosition().y)) {
			((Ball) objectA).paddleRebound((Paddle )objectB);
            ballVel.set(bodyA.getLinearVelocity());
            if (objectB == paddleOne)
                botSound.play();
            else
                topSound.play();
		}

		//Sets separate offsets for the AI whenever a different paddle is hit.
		if ((objectA instanceof Ball && objectB == paddleOne || objectA == paddleOne && objectB instanceof Ball) &&
                paddleTwo instanceof AIPaddle) {
            ((AIPaddle) paddleTwo).randomizeOffset();
        } else if ((objectA instanceof Ball && objectB == paddleTwo || objectA == paddleTwo && objectB instanceof Ball) &&
                paddleOne instanceof AIPaddle) {
            ((AIPaddle) paddleOne).randomizeOffset();
        }

        //if collision is between paddle and ball, and paddleTwo is null, meaning it's wall mode, score
        if ((objectA instanceof Ball && objectB == paddleOne || objectA == paddleOne && objectB instanceof Ball) &&
                paddleTwo == null) {
		    screen.score("bot");
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
//        Object objectA = contact.getFixtureA().getBody().getUserData(); //These might be redundant
//        Object objectB = contact.getFixtureB().getBody().getUserData();
//        Body bodyA = contact.getFixtureA().getBody();
//        Body bodyB = contact.getFixtureB().getBody();
//        if (objectA instanceof Paddle && objectB instanceof Ball) {
//            bodyB.setLinearVelocity(ballVel);
//        } else if (objectB instanceof Paddle && objectA instanceof Ball) {
//            bodyA.setLinearVelocity(ballVel);
//        }
	}
}
