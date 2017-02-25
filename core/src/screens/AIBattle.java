package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.powerpong.game.ContactListener;
import com.powerpong.game.PowerPong;
import objects.paddles.AIPaddle;
import objects.Ball;

public class AIBattle extends PlayScreen {

    private ContactListener contactListener;

    public AIBattle(PowerPong game, AI ai) {
        super(game);

        ball = new Ball("ClassicBall.png", 0, 0, BALL_DIRECTION, BALL_SPEED, world);
        p1 = new AIPaddle("ClassicPaddle.png", 0, -1100 / PowerPong.PPM, world, ball, ai);
        p2 = new AIPaddle("ClassicPaddle.png", 0, 1100 / PowerPong.PPM, world, ball, ai);

        contactListener = new ContactListener(p1, p2);
        world.setContactListener(contactListener);

        multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(this);
    }

    //empty pauseBall() method so that when it's called for AIBattle, the ball isn't actually ballPaused
    public void pauseBall() {

    }
}
