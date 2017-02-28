package com.powerpong.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import objects.Wall;
import objects.paddles.AIPaddle;
import objects.Ball;
import objects.paddles.Paddle;
import screens.PlayScreen;
import com.powerpong.game.Options.Mode;


public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {
	private Paddle paddleOne, paddleTwo;
	//private Vector2 ballVel;
	private PlayScreen screen;
	private Options options;

	private Sound botSound, topSound;


    public ContactListener(Paddle paddleOne, Paddle paddleTwo, PlayScreen screen) {
        this.paddleOne = paddleOne;
        this.paddleTwo = paddleTwo;
        this.screen = screen;
        this.options = screen.options;
        //ballVel = new Vector2();

        botSound = Gdx.audio.newSound(Gdx.files.internal("sounds/botboop.wav")); //TODO: dispose this later
        topSound = Gdx.audio.newSound(Gdx.files.internal("sounds/topboop.wav")); //TODO: dispose this later
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
            //ballVel.set(bodyB.getLinearVelocity());
            if (options.soundOn) {
                if (objectA == paddleOne)
                    botSound.play();
                else
                    topSound.play();
            }
		} else if (objectB instanceof Paddle && objectA instanceof Ball &&
                Math.abs(bodyA.getPosition().y) < Math.abs(bodyB.getPosition().y)) {
			((Ball) objectA).paddleRebound((Paddle )objectB);
            //ballVel.set(bodyA.getLinearVelocity());
            if (options.soundOn) {
                if (objectB == paddleOne)
                    botSound.play();
                else
                    topSound.play();
            }
		}

		//Sets separate offsets for the AI whenever a different paddle is hit.
		if ((objectA instanceof Ball && objectB == paddleOne || objectA == paddleOne && objectB instanceof Ball) &&
                paddleTwo instanceof AIPaddle) {
            ((AIPaddle) paddleTwo).randomizeOffset();
        } else if ((objectA instanceof Ball && objectB == paddleTwo || objectA == paddleTwo && objectB instanceof Ball) &&
                paddleOne instanceof AIPaddle) {
            ((AIPaddle) paddleOne).randomizeOffset();
        }

        //if it's survival mode, and collision is between paddle and ball, score.
        if (screen.getMode() == Mode.SURVIVAL && (objectA instanceof Ball && objectB == paddleOne || objectA == paddleOne && objectB instanceof Ball)) {
		    screen.score("bot");
        }

        if (screen.getMode() == Mode.PRACTICE){
			if(objectA instanceof Ball && objectB instanceof Wall && ((Wall) objectB).getNinePatch() != null) {
				((Wall) objectB).needsNewLocation(true);
				screen.score("bot");
			}
			else if (objectA instanceof Wall && objectB instanceof Ball && ((Wall) objectA).getNinePatch() != null) {
				((Wall) objectA).needsNewLocation(true);
				screen.score("bot");
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
