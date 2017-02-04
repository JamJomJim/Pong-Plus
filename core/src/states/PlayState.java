package states;

import java.util.Timer;
import java.util.TimerTask;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
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
import com.powerpong.game.MapBodyManager;
import com.powerpong.game.MyContactListener;
import com.powerpong.game.PowerPong;
import objects.Paddle;
import objects.PlayerPaddle;

public class PlayState implements State {
	static final float GRAVITY = 0f; //-9.8 is -9.8m/s^2, as in real life. I think.

	private Paddle p1;

	private SpriteBatch batch;
	private World world;
	private OrthographicCamera worldCam, uiCam;
	private Box2DDebugRenderer debugRenderer;
	private MyContactListener contactListener;
	private FPSLogger fps = new FPSLogger();
	private MapBodyManager mbm;
	private TiledMap tiledMap;
	private TiledMapRenderer tiledMapRenderer;
	private BitmapFont font;
	private GameStateManager gsm;

	public PlayState(GameStateManager gsm) {
		this.gsm = gsm;
		batch = new SpriteBatch();
		font = new BitmapFont();

		//create physics world and contactlistener
		world = new World(new Vector2(0, GRAVITY), true);
		//create paddle(s) in physics world
		p1 = new PlayerPaddle("ClassicPaddle.png", 0, 0, world);

		//create InputMultiplexer, to handle input on multiple paddles and the ui
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(p1);

		contactListener = new MyContactListener();
		world.setContactListener(contactListener);

		//cam stuff
		//using a constant viewport solves the problem of scaling on different resolutions
		worldCam = new OrthographicCamera(PowerPong.WIDTH / PowerPong.PIXELS_IN_METER,
				PowerPong.HEIGHT / PowerPong.PIXELS_IN_METER); //scale camera viewport to meters
		System.out.println(Gdx.graphics.getHeight());
		//worldCam.position.set(p1.getX(), p1.getY(), 0); //center camera on player
		uiCam = new OrthographicCamera(PowerPong.WIDTH, PowerPong.HEIGHT);
		//uiCam.position.set(player.getCenter().x, player.getCenter().y, 0); //center camera on player

		debugRenderer = new Box2DDebugRenderer();

		//load map and create static bodies for its tiles
		/*mbm = new MapBodyManager(world, PowerPong.PIXELS_IN_METER, null, 1);
		tiledMap = new TmxMapLoader().load("jamesMap.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / PowerPong.PIXELS_IN_METER);
		mbm.createPhysics(tiledMap);*/

		//schedule physics simulation to run every ~1/60th of a second
		new Timer().scheduleAtFixedRate(new TimerTask() {
			public void run() { //the stuff it does each time it runs
				world.step(0.016f, 6, 2);
			}
		}
		, 0L //delay before first run, in milliseconds
		, 16L //time between runs, in milliseconds; value of 16 results in 62.5 runs per second
				);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//draw the TiledMap
		/*tiledMapRenderer.setView(worldCam);
		tiledMapRenderer.render();*/

		//draw the world
		//current coordinate system is 0,0 is the center of the screen, positive y is up
		batch.setProjectionMatrix(worldCam.combined);
		batch.begin();
		p1.draw(batch);
		batch.end();

		//draw the ui; positions in this are relative to the screen, regardless of where the worldCam might be.
		//drawing something at (0, 0) will draw it in the center of the screen
		//drawing something at (-Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2) will draw it in the top left
		//and so on
		//note that text is drawn starting at the top left corner of it. So if you try drawing in any corner besides the top left without accounting for that,
		//the text will be off-screen
		batch.setProjectionMatrix(uiCam.combined);
		batch.begin();
		//draw something in top left for debug purposes
		font.draw(batch, "hi", -PowerPong.WIDTH / 2 + 5, PowerPong.HEIGHT / 2 - 10);
		batch.end();

		//render fixtures from world; scaled properly because it uses the projection matrix from worldCam, which is scaled properly
		debugRenderer.render(world, worldCam.combined);
		//log fps to console
		//fps.log();
	}

	public void dispose() {
		batch.dispose();
		world.dispose();
		p1.dispose();
		font.dispose();
		debugRenderer.dispose();
	}
}