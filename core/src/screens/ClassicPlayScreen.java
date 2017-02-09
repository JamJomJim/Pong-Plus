package screens;

import java.util.Timer;
import java.util.TimerTask;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.powerpong.game.MapBodyManager;
import com.powerpong.game.MyContactListener;
import com.powerpong.game.PowerPong;
import objects.*;

public class ClassicPlayScreen extends PlayScreen {

	public ClassicPlayScreen(PowerPong game, AI ai) {
		super(game, ai);
		ball = new Ball("ClassicBall.png", 0, 0, BALL_DIRECTION, BALL_SPEED, world, this);
		p1 = new PlayerPaddle("ClassicPaddle.png", 0, -1100 / PowerPong.PPM, world, worldCam);
		if (ai == AI.NONE)
			p2 = new PlayerPaddle("ClassicPaddle.png", 0, 1100 / PowerPong.PPM, world, worldCam);
		else
			p2 = new AIPaddle("ClassicPaddle.png", 0, 1100 / PowerPong.PPM, world, ball, ai);
        //create InputMultiplexer, to handle input on multiple paddles and the ui
        multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(p1);
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        if (ai == AI.NONE)
            multiplexer.addProcessor(p2);
	}
}