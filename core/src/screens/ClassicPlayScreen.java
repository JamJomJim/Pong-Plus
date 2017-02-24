package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.powerpong.game.ContactListener;
import com.powerpong.game.PowerPong;
import objects.*;
import objects.paddles.*;

public class ClassicPlayScreen extends PlayScreen {

	private ContactListener contactListener;

	public ClassicPlayScreen(PowerPong game, AI ai) {
		super(game);

		ball = new Ball("ClassicBall.png", 0, 0, BALL_DIRECTION, BALL_SPEED, world);
		p1 = new PlayerPaddle("ClassicPaddle.png", 0, -1100 / PowerPong.PPM, world, worldCam);
		if (ai == AI.NONE)
			p2 = new PlayerPaddle("ClassicPaddle.png", 0, 1100 / PowerPong.PPM, world, worldCam);
		else
			p2 = new AIPaddle("ClassicPaddle.png", 0, 1100 / PowerPong.PPM, world, ball, ai);
		contactListener = new ContactListener(p1, p2);
		world.setContactListener(contactListener);
        //create InputMultiplexer, to handle input on multiple paddles and the ui
        multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
		multiplexer.addProcessor(this); //playscreen is first in multiplexer, for handling resuming ball
        multiplexer.addProcessor(p1);
        multiplexer.addProcessor(stage);
        if (ai == AI.NONE)
            multiplexer.addProcessor(p2);

        pauseBall();
	}
}