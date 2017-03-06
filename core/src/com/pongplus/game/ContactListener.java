package com.pongplus.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.pongplus.game.Options.Mode;
import objects.Ball;
import objects.Wall;
import objects.paddles.AIPaddle;
import objects.paddles.Paddle;
import screens.PlayScreen;


public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {
    private Paddle paddleOne, paddleTwo;
    private PlayScreen screen;
    private Options options;

    private Sound botSound, topSound;


    public ContactListener(Paddle paddleOne, Paddle paddleTwo, PlayScreen screen) {
        this.paddleOne = paddleOne;
        this.paddleTwo = paddleTwo;
        this.screen = screen;
        this.options = screen.options;

        botSound = Gdx.audio.newSound(Gdx.files.internal("sounds/botboop.wav"));
        topSound = Gdx.audio.newSound(Gdx.files.internal("sounds/topboop.wav"));
    }

    @Override
    public void beginContact(Contact contact) {
        Object objectA = contact.getFixtureA().getBody().getUserData();
        Object objectB = contact.getFixtureB().getBody().getUserData();
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        //if the collision is between a paddle and the ball and the ball is on the "inside" of the paddle, rebound the ball appropriately
        if (objectA instanceof Paddle && objectB instanceof Ball &&
                Math.abs(bodyB.getPosition().y) < Math.abs(bodyA.getPosition().y)) {
            ((Ball) objectB).paddleRebound((Paddle) objectA);
            if (options.soundOn) {
                if (objectA == paddleOne)
                    botSound.play();
                else
                    topSound.play();
            }
        } else if (objectB instanceof Paddle && objectA instanceof Ball &&
                Math.abs(bodyA.getPosition().y) < Math.abs(bodyB.getPosition().y)) {
            ((Ball) objectA).paddleRebound((Paddle) objectB);
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

        if (screen.getMode() == Mode.TARGETS) {
            if (objectA instanceof Ball && objectB instanceof Wall && ((Wall) objectB).getNinePatch() != null ||
                    objectA instanceof Wall && objectB instanceof Ball && ((Wall) objectA).getNinePatch() != null) {

                if (objectA instanceof Wall)
                    ((Wall) objectA).needsNewLocation(true);
                else
                    ((Wall) objectB).needsNewLocation(true);

                int amount = 3 - Math.round(Math.abs((bodyB.getPosition().x - bodyA.getPosition().x) / (options.targetWidth / 2 / PongPlus.PPM)) * 3);
                if (amount <= 0) amount = 1;
                screen.score("bot", amount);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {


    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {


    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public void dispose() {
        botSound.dispose();
        topSound.dispose();
    }
}
