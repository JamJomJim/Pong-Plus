package screens;

import com.powerpong.game.ContactListener;
import com.powerpong.game.PowerPong;
import objects.AIPaddle;
import objects.Ball;

public class AIBattle extends PlayScreen {

    private ContactListener contactListener;

    public AIBattle(PowerPong game, AI ai) {
        super(game, ai);

        ball = new Ball("ClassicBall.png", 0, 0, BALL_DIRECTION, BALL_SPEED, world, this);
        p1 = new AIPaddle("ClassicPaddle.png", 0, -1100 / PowerPong.PPM, world, ball, ai);
        p2 = new AIPaddle("ClassicPaddle.png", 0, 1100 / PowerPong.PPM, world, ball, ai);

        contactListener = new ContactListener(p1, p2);
        world.setContactListener(contactListener);
    }
}
