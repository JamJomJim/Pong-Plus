package states;

import java.util.Timer;
import java.util.TimerTask;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.powerpong.game.MapBodyManager;
import com.powerpong.game.MyContactListener;
import com.powerpong.game.PowerPong;

public class PlayState implements State {
	static final float GRAVITY = -9.8f; //-9.8 is -9.8m/s^2, as in real life. I think.

	SpriteBatch batch;
	World world;
	Vector3 playerVector3;
	OrthographicCamera worldCam, uiCam;
	Box2DDebugRenderer debugRenderer;
	MapBodyManager mbm;
	MyContactListener contactListener;
	FPSLogger fps = new FPSLogger();
	TiledMap tiledMap;
	TiledMapRenderer tiledMapRenderer;
	BitmapFont font;
	GameStateManager gsm;

	public PlayState(GameStateManager gsm) {
		this.gsm = gsm;
		batch = new SpriteBatch();
		font = new BitmapFont();
		playerVector3 = new Vector3(0, 0, 0);

		//create physics world and contactlistener
		world = new World(new Vector2(0, GRAVITY), true);
		//create player in physics world

		contactListener = new MyContactListener();
		world.setContactListener(contactListener);

		//cam stuff
		worldCam = new OrthographicCamera(1080 / PowerPong.PIXELS_IN_METER, 1920 / PowerPong.PIXELS_IN_METER); //scale camera viewport to meters
		//worldCam.position.set(player.getCenter().x, player.getCenter().y, 0); //center camera on player
		uiCam = new OrthographicCamera(640, 480);
		//uiCam.position.set(player.getCenter().x, player.getCenter().y, 0); //center camera on player

		debugRenderer = new Box2DDebugRenderer();

		//load map and create static bodies for its tiles
		/*mbm = new MapBodyManager(world, PowerPong.PIXELS_IN_METER, null, 1);
		tiledMap = new TmxMapLoader().load("jamesMap.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / PowerPong.PIXELS_IN_METER);
		mbm.createPhysics(tiledMap);*/

		//schedule physics simulation to run every ~1/60th of a second
		new Timer().scheduleAtFixedRate(new TimerTask() {
			public void run() {
				world.step(0.016f, 6, 2);
			}
		}
		, 0L //delay before first run, in milliseconds
		, 16L //time between runs, in milliseconds; value of 16 results in 62.5 runs per second
				);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//draw the TiledMap
		/*tiledMapRenderer.setView(worldCam);
		tiledMapRenderer.render();*/

		//draw the world
		batch.setProjectionMatrix(worldCam.combined);
		batch.begin();
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
		font.draw(batch, "hi", -Gdx.graphics.getWidth() / 2 + 5, Gdx.graphics.getHeight() / 2 - 5);
		batch.end();

		//render fixtures from world; scaled properly because it uses the projection matrix from worldCam, which is scaled properly
		debugRenderer.render(world, worldCam.combined);
		//log fps to console
		//fps.log();
	}

	public void dispose() {
		batch.dispose();
	}
}