package screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.powerpong.game.ContactListener;
import com.powerpong.game.PowerPong;
import objects.*;

/**
 * Created by Nick on 2/7/2017.
 */
public class PlayScreen extends InputAdapter implements Screen {
    static final float GRAVITY = 0f; //-9.8 is -9.8m/s^2, as in real life. I think.

    protected float BALL_DIRECTION = (float)Math.PI * 3 / 2;
    protected float BALL_SPEED = 3;

    public enum AI {
        NONE, EASY, MEDIUM, HARD, SKYNET
    }

    protected Paddle p1, p2;
    protected Ball ball;

    private int topScore = 0;
    private int botScore = 0;

    private Box2DDebugRenderer debugRenderer;
    private ContactListener contactListener;
    private BitmapFont font;
    private PowerPong game;

    protected InputMultiplexer multiplexer;
    protected Stage stage;
    protected Skin skin;
    protected Label topScoreText;
    protected Label botScoreText;
    protected World world;
    protected OrthographicCamera worldCam, uiCam;

    protected PlayScreen() {
    }

    protected PlayScreen(PowerPong game, AI ai) {
        this.game = game;
        font = new BitmapFont();
        font.getData().setScale(8);

        //create physics world and contactlistener
        world = new World(new Vector2(0, GRAVITY), true);
        contactListener = new ContactListener();
        world.setContactListener(contactListener);

        worldCam = new OrthographicCamera(PowerPong.NATIVE_WIDTH / PowerPong.PPM,
                PowerPong.NATIVE_HEIGHT / PowerPong.PPM); //scale camera viewport to meters
        uiCam = new OrthographicCamera(PowerPong.NATIVE_WIDTH, PowerPong.NATIVE_HEIGHT);
        stage = new Stage(new FitViewport(PowerPong.NATIVE_WIDTH, PowerPong.NATIVE_HEIGHT), game.batch);

        topScore = 0;
        botScore = 0;

        //right wall
        new Wall((PowerPong.NATIVE_WIDTH + 2) / PowerPong.PPM / 2, 0, 1, PowerPong.NATIVE_HEIGHT, 0, world);
        //left wall
        new Wall((-PowerPong.NATIVE_WIDTH - 2) / PowerPong.PPM / 2, 0, 1, PowerPong.NATIVE_HEIGHT, 0, world);

        //stage stuff for the ui
        skin = new Skin(Gdx.files.internal("skins/neon/neon-ui.json"));
        // Generate a font and add it to the skin under the name "Xcelsion"
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Xcelsion.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        skin.add("Xcelsion", generator.generateFont(parameter));

        //get the TextButtonStyle defined in the JSON under the name "default" and then modify it
        TextButton.TextButtonStyle style = skin.get("default", TextButton.TextButtonStyle.class);
        style.font = skin.getFont("Xcelsion");

        stage = new Stage(new FitViewport(PowerPong.NATIVE_WIDTH, PowerPong.NATIVE_HEIGHT), game.batch);
        stage.setDebugAll(true);
        Table table = new Table();
        table.setFillParent(true);
        table.align(Align.left);
        stage.addActor(table);

        VerticalGroup score = new VerticalGroup();
        //table.add(score);
        topScoreText = new Label(Integer.toString(topScore), skin);
        botScoreText = new Label(Integer.toString(botScore), skin);
        topScoreText.setFontScale(5);
        botScoreText.setFontScale(10);
        table.add(topScoreText);
        table.row();
        table.add(botScoreText);

        debugRenderer = new Box2DDebugRenderer();
    }

    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //step the physics world the amount of time since the last frame, up to 0.25s
        world.step((float)Math.min(dt, 0.25), 6 ,2);
        p1.update(dt);
        p2.update(dt);
        ball.update();
        stage.act(dt);
        topScoreText.setText(Integer.toString(topScore));
        botScoreText.setText(Integer.toString(botScore));

        stage.draw();
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
        font.draw(game.batch, Integer.toString(botScore), 0, PowerPong.NATIVE_HEIGHT / -4);
        font.draw(game.batch, Integer.toString(topScore), 0, PowerPong.NATIVE_HEIGHT / 4);
        game.batch.end();

        //render fixtures from world; scaled properly because it uses the projection matrix from worldCam, which is scaled properly
        debugRenderer.render(world, worldCam.combined);
    }

    public void score(String side) {
        if (side.equals("top"))
            topScore += 1;
        else if (side.equals("bot"))
            botScore += 1;
    }

    @Override
    public void show() {
        game.batch.setProjectionMatrix(worldCam.combined);
        Gdx.input.setInputProcessor(multiplexer);
        Gdx.input.setCatchBackKey(true);
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

    @Override
    public void dispose() {
        world.dispose();
        p1.dispose();
        font.dispose();
        debugRenderer.dispose();
        ball.dispose();
        stage.dispose();
        p2.dispose();
    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.BACK || keyCode == Input.Keys.ESCAPE) {
            dispose();
            game.setScreen(new MenuScreen(game));
        }
        return super.keyDown(keyCode);
    }
}
