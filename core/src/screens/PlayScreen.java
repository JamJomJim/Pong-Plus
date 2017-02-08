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

public class PlayScreen implements Screen {
	static final float GRAVITY = 0f; //-9.8 is -9.8m/s^2, as in real life. I think.
	private int BALL_DIRECTION = -90; //(int)(Math.floor(Math.random() * 2) * 2 - 1) * 90; //in degrees
	private float BALL_SPEED = 5;

	private Paddle p1, p2;
	private Ball ball;

	private int topScore, botScore;

	private World world;
	private OrthographicCamera worldCam, uiCam;
	private Box2DDebugRenderer debugRenderer;
	private MyContactListener contactListener;
	private FPSLogger fps = new FPSLogger();
	private MapBodyManager mbm;
	private TiledMap tiledMap;
	private TiledMapRenderer tiledMapRenderer;
	private BitmapFont font;
	private PowerPong game;

	public PlayScreen(PowerPong game, String mode) {
		this.game = game;
		font = new BitmapFont();
		font.getData().setScale(2);

		//create physics world and contactlistener
		world = new World(new Vector2(0, GRAVITY), true);

		contactListener = new MyContactListener();
		world.setContactListener(contactListener);

		//cam stuff
		//using a constant-size viewport solves the problem of scaling on different resolutions
		//the viewport will be sized to take up the entire space of what the application is occupying
		//down/up-scaling will be done automatically
		worldCam = new OrthographicCamera(PowerPong.NATIVE_WIDTH / PowerPong.PPM,
				PowerPong.NATIVE_HEIGHT / PowerPong.PPM); //scale camera viewport to meters
		uiCam = new OrthographicCamera(PowerPong.NATIVE_WIDTH, PowerPong.NATIVE_HEIGHT);
		ball = new Ball("Ball.png", 0, 0, BALL_DIRECTION, BALL_SPEED, world, this);
		p1 = new PlayerPaddle("ClassicPaddle.png", 0, -1100 / PowerPong.PPM, world, worldCam);
		if (mode.equals("1P"))
			p2 = new AIPaddle("ClassicPaddle.png", 0, 1100 / PowerPong.PPM, world, ball, AIPaddle.Diff.HARD);
		else if (mode.equals("2P"))
			p2 = new PlayerPaddle("ClassicPaddle.png", 0, 1100 / PowerPong.PPM, world, worldCam);

		topScore = 0;
		botScore = 0;

		//right wall
		new Wall((PowerPong.NATIVE_WIDTH + 2) / PowerPong.PPM / 2, 0, 1, PowerPong.NATIVE_HEIGHT, 0, world);
		//left wall
		new Wall((-PowerPong.NATIVE_WIDTH - 2) / PowerPong.PPM / 2, 0, 1, PowerPong.NATIVE_HEIGHT, 0, world);

		//create InputMultiplexer, to handle input on multiple paddles and the ui
		InputMultiplexer multiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(multiplexer);
		multiplexer.addProcessor(p1);
		if (mode.equals("2P"))
			multiplexer.addProcessor(p2);

		debugRenderer = new Box2DDebugRenderer();

		//load map and create static bodies for its tiles
		/*mbm = new MapBodyManager(world, PowerPong.PPM, null, 1);
		tiledMap = new TmxMapLoader().load("jamesMap.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / PowerPong.PPM);
		mbm.createPhysics(tiledMap);*/
	}

	@Override
	public void show() {
		game.batch.setProjectionMatrix(worldCam.combined);
	}

	@Override
	public void render(float dt) {
		//step the physics world the amount of time since the last frame, up to 0.25s
		world.step((float)Math.min(dt, 0.25), 6 ,2);
		p1.update(dt);
		p2.update(dt);
		ball.update();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//draw the TiledMap
		/*tiledMapRenderer.setView(worldCam);
		tiledMapRenderer.render();*/

		//draw the world
		//current coordinate system is 0,0 is the center of the screen, positive y is up
		game.batch.setProjectionMatrix(worldCam.combined);
		game.batch.begin();
		p1.draw(game.batch);
		p2.draw(game.batch);
		ball.draw(game.batch);
		game.batch.end();
		//draw the ui; positions in this are relative to the screen, regardless of where the worldCam might be.
		//drawing something at (0, 0) will draw it in the center of the screen
		//drawing something at (-Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2) will draw it in the top left
		//and so on
		//note that text is drawn starting at the top left corner of it. So if you try drawing in any corner besides the top left without accounting for that,
		//the text will be off-screen
		game.batch.setProjectionMatrix(uiCam.combined);
		game.batch.begin();
		//draw something in top left for debug purposes
		font.draw(game.batch, "top score: " + topScore + "  bot score: " + botScore, -PowerPong.NATIVE_WIDTH / 2 + 5, PowerPong.NATIVE_HEIGHT / 2 - 10);
		game.batch.end();

		//render fixtures from world; scaled properly because it uses the projection matrix from worldCam, which is scaled properly
		debugRenderer.render(world, worldCam.combined);
		//log fps to console
		//fps.log();
	}

	public void score(String side) {
		if (side.equals("top"))
			topScore += 1;
		else if (side.equals("bot"))
			botScore += 1;
		//resets the offset of the AIPaddle
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	public void dispose() {
		world.dispose();
		p1.dispose();
		font.dispose();
		debugRenderer.dispose();
	}
}