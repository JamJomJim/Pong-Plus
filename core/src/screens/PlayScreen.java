package screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.powerpong.game.PowerPong;
import objects.*;
import objects.paddles.Paddle;

public class PlayScreen extends InputAdapter implements Screen {
    static final float GRAVITY = 0f; //-9.8 is -9.8m/s^2, as in real life. I think.

    protected float BALL_DIRECTION = (float)Math.PI * 3 / 2;
    protected float BALL_SPEED = 3;
    protected Vector2 ballVel;
    protected boolean paused;

    public enum AI {
        NONE, EASY, MEDIUM, HARD, SKYNET
    }

    protected Paddle p1, p2;
    protected Ball ball;

    private int topScore = 0;
    private int botScore = 0;

    protected Box2DDebugRenderer debugRenderer;
    protected PowerPong game;

    protected InputMultiplexer multiplexer;
    protected Stage stage;
    protected Skin skin;
    protected Label topScoreText, botScoreText;
    protected World world;
    protected OrthographicCamera worldCam;

    protected PlayScreen() {
    }

    protected PlayScreen(PowerPong game) {
        this.game = game;

        //create physics world and contactlistener
        world = new World(new Vector2(0, GRAVITY), true);
        world.setVelocityThreshold(0.01f);

        ballVel = new Vector2();

        worldCam = new OrthographicCamera(PowerPong.NATIVE_WIDTH / PowerPong.PPM,
                PowerPong.NATIVE_HEIGHT / PowerPong.PPM); //scale camera viewport to meters

        stage = new Stage(new FitViewport(PowerPong.NATIVE_WIDTH, PowerPong.NATIVE_HEIGHT), game.batch);

        topScore = 0;
        botScore = 0;

        new Wall((PowerPong.NATIVE_WIDTH + 2) / PowerPong.PPM / 2, 0, 1, PowerPong.NATIVE_HEIGHT, 0, world); //right wall
        new Wall((-PowerPong.NATIVE_WIDTH - 2) / PowerPong.PPM / 2, 0, 1, PowerPong.NATIVE_HEIGHT, 0, world); //left wall

        //stage stuff for the ui
        skin = new Skin(Gdx.files.internal("skins/neon/neon-ui.json"));
        // Generate a font and add it to the skin under the name "Xcelsion"
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arial.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 150;
        skin.add("Arial", generator.generateFont(parameter));

        //get the TextButtonStyle defined in the JSON under the name "default" and then modify it
        Label.LabelStyle labelStyle = skin.get("default", Label.LabelStyle.class);
        labelStyle.font = skin.getFont("Arial");
        labelStyle.fontColor = Color.WHITE;

        //create the stage for ui elements
        stage = new Stage(new FitViewport(PowerPong.NATIVE_WIDTH, PowerPong.NATIVE_HEIGHT), game.batch);
        stage.setDebugAll(true);
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        //create the table and the labels that will display the score
        Table score = new Table();
        topScoreText = new Label(Integer.toString(topScore), skin);
        botScoreText = new Label(Integer.toString(botScore), skin);
        score.add(topScoreText).right();
        score.row();
        score.add(botScoreText).right();

        table.add(score);
        table.right();

        debugRenderer = new Box2DDebugRenderer(); //displays hitboxes in order to see what bodies "look like"
    }

    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //step the physics world the amount of time since the last frame, up to 0.25s
        world.step((float)Math.min(dt, 0.25), 6 ,2);

        checkBall();
        p1.update(dt);
        p2.update(dt);
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

        //render fixtures from world; scaled properly because it uses the projection matrix from worldCam, which is scaled properly
        debugRenderer.render(world, worldCam.combined);
    }

    public void checkBall() {
        Body body = ball.getBody();
        int direction;
        if (body.getPosition().y < -PowerPong.NATIVE_HEIGHT / 2 / PowerPong.PPM) {
            score("top");
            direction = -1;
        }
        else if (body.getPosition().y > PowerPong.NATIVE_HEIGHT / 2 / PowerPong.PPM) {
            score("bot");
            direction = 1;
        }
        else return;
        ball.reset(direction);
        pauseBall();
    }

    public void pauseBall() {
        //if statement is so that if the ball is already paused, ballVel won't be set to 0, meaning the ball couldn't be "resumed"
        if (ball.getBody().getLinearVelocity().y != 0)
            ballVel.set(ball.getBody().getLinearVelocity());
        ball.getBody().setLinearVelocity(0, 0);
        paused = true;
    }

    public void resumeBall() {
        ball.getBody().setLinearVelocity(ballVel);
        paused = false;
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

    //pause() is called when the application loses focus/is no longer active
    @Override
    public void pause() {

    }

    //resume() is called when the app regains focus/is active again
    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void dispose() {
        world.dispose();
        p1.dispose();
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

    public boolean touchDown(int x, int y, int pointer, int button) {
        if (paused) {
            resumeBall();
            return true;
        }
        return false;
    }
}
