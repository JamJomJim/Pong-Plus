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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.powerpong.game.ContactListener;
import com.powerpong.game.PowerPong;
import objects.*;
import objects.paddles.AIPaddle;
import objects.paddles.Paddle;
import objects.paddles.PlayerPaddle;

public class PlayScreen extends InputAdapter implements Screen {
    static final float GRAVITY = 0f; //-9.8 is -9.8m/s^2, as in real life. I think.

    public enum AI {//different AI difficulties
        NONE, EASY, MEDIUM, HARD, SKYNET
    }

    public enum Mode {//different modes of play
        ONEPLAYER, TWOPLAYER, SURVIVAL, AIBATTLE, MENUBATTLE
    }

    //ball stuff
    protected float BALL_DIRECTION = (float)Math.PI * 3 / 2;
    protected float BALL_SPEED = 3;
    protected Ball ball;

    Mode mode;

    protected Paddle p1, p2;

    protected int topScore = 0;
    protected int botScore = 0;

    //game world stuff
    protected PowerPong game;
    protected World world;
    protected OrthographicCamera worldCam;
    protected Box2DDebugRenderer debugRenderer;

    //ui stuff
    protected InputMultiplexer multiplexer;
    protected Stage stage;
    protected Label topScoreText, botScoreText, pausedText;
    protected Table score, menu;


    protected PlayScreen(PowerPong game, Mode mode, final AI ai) {
        this.game = game;
        this.mode = mode;

        //GAME WORLD STUFF**********************************************************************************************
        //create physics world and contactlistener
        world = new World(new Vector2(0, GRAVITY), true);
        world.setVelocityThreshold(0.01f);

        stage = new Stage(new FitViewport(PowerPong.NATIVE_WIDTH, PowerPong.NATIVE_HEIGHT), game.batch);

        worldCam = new OrthographicCamera(PowerPong.NATIVE_WIDTH / PowerPong.PPM,
                PowerPong.NATIVE_HEIGHT / PowerPong.PPM); //scale camera viewport to meters

        topScore = 0;
        botScore = 0;

        //create the side walls (and top wall if it's survival mode)
        new Wall((PowerPong.NATIVE_WIDTH + 2) / PowerPong.PPM / 2, 0, 1, PowerPong.NATIVE_HEIGHT, 0, world); //right wall
        new Wall((-PowerPong.NATIVE_WIDTH - 2) / PowerPong.PPM / 2, 0, 1, PowerPong.NATIVE_HEIGHT, 0, world); //left wall
        if (mode == Mode.SURVIVAL)
            new Wall(0, PowerPong.NATIVE_HEIGHT / PowerPong.PPM / 2 + 1, PowerPong.NATIVE_WIDTH, 1, 0, world);

        //create the ball
        ball = new Ball("ClassicBall.png", 0, 0, BALL_DIRECTION, BALL_SPEED, world);

        //create p1 depending on the mode
        if (mode == Mode.AIBATTLE || mode == Mode.MENUBATTLE)
            p1 = new AIPaddle("ClassicPaddle.png", 0, -1100 / PowerPong.PPM, world, ball, ai);
        else
            p1 = new PlayerPaddle("ClassicPaddle.png", 0, -1100 / PowerPong.PPM, world, worldCam);

        //create p2 depending on the mode
        if (mode == Mode.TWOPLAYER)
            p2 = new PlayerPaddle("ClassicPaddle.png", 0, 1100 / PowerPong.PPM, world, worldCam);
        else if (mode == Mode.SURVIVAL)
            p2 = null;
        else
            p2 = new AIPaddle("ClassicPaddle.png", 0, 1100 / PowerPong.PPM, world, ball, ai);

        world.setContactListener(new ContactListener(p1, p2, this));

        //create InputMultiplexer, to handle input on multiple paddles and the ui
        multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(this); //playscreen is first in multiplexer, for handling resuming ball
        multiplexer.addProcessor(stage);
        if (p1 instanceof PlayerPaddle)
            multiplexer.addProcessor(p1);
        if (p2 instanceof PlayerPaddle)
            multiplexer.addProcessor(p2);

        if (p1 instanceof PlayerPaddle)
            ball.pause(); //ball starts paused
        debugRenderer = new Box2DDebugRenderer(); //displays hitboxes in order to see what bodies "look like"

        //UI STUFF******************************************************************************************************
        //create and add the table that fills the entire screen
        stage.setDebugAll(true);
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        //create the table and the labels that will display the score
        score = new Table();
        score.setSkin(game.skin);
        topScoreText = new Label(Integer.toString(topScore), game.skin);
        botScoreText = new Label(Integer.toString(botScore), game.skin);
        score.add(topScoreText).right();
        score.row();
        score.add(botScoreText).right();
        //add it to the stage and position it
        stage.addActor(score);
        score.setX(PowerPong.NATIVE_WIDTH - score.getPrefWidth() / 2);
        score.setY(PowerPong.NATIVE_HEIGHT / 2);
        if (mode == Mode.MENUBATTLE)
            score.setVisible(false);

        //if it's one or two player mode, create the menu that appears when a score reaches 10
        if (mode == Mode.ONEPLAYER || mode == Mode.TWOPLAYER) {
            menu = new Table();
            final TextButton buttonRestart = new TextButton("Play Again", game.skin);
            buttonRestart.setHeight(175);
            buttonRestart.setWidth(buttonRestart.getPrefWidth() + 50);
            menu.add(buttonRestart).width(buttonRestart.getWidth()).height(buttonRestart.getHeight());
            menu.row();
            final TextButton buttonMenu = new TextButton("Menu", game.skin);
            menu.add(buttonMenu).fillX().height(buttonRestart.getHeight());
            buttonRestart.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    topScore = 0;
                    botScore = 0;
                    p1.getBody().setTransform(0, p1.getBody().getPosition().y, 0);
                    p2.getBody().setTransform(0, p2.getBody().getPosition().y, 0);
                    ball.reset(-1);
                    ball.pause();
                    menu.setVisible(false);
                    buttonRestart.setChecked(false);
                }
            });
            //menu.setVisible(false);
            stage.addActor(menu);
            menu.setX(PowerPong.NATIVE_WIDTH / 2);
            menu.setY(PowerPong.NATIVE_HEIGHT / 2);
        }
        //if it's a mode that includes a player (aka it can be paused), create the label that's displayed during pause
        if (p1 instanceof PlayerPaddle) {
            pausedText = new Label("Paused", game.skin);
            pausedText.setVisible(false);
            stage.addActor(pausedText);
            pausedText.setX(PowerPong.NATIVE_WIDTH / 2 - pausedText.getPrefWidth() / 2);
            pausedText.setY(PowerPong.NATIVE_HEIGHT / 2 - pausedText.getPrefHeight() / 2);
        }
    }

    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //step the physics world the amount of time since the last frame, up to 0.25s
        world.step((float)Math.min(dt, 0.25), 6 ,2);

        checkBall();
        checkScore();
        p1.update(dt);
        if (mode != Mode.SURVIVAL)
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
        if (mode != Mode.SURVIVAL)
            p2.draw(game.batch);
        ball.draw(game.batch);
        game.batch.end();

        //render fixtures from world; scaled properly because it uses the projection matrix from worldCam, which is scaled properly
        debugRenderer.render(world, worldCam.combined);
    }

    public void checkScore() {
        if ((botScore >= 10 || topScore >= 10) && menu != null)
            menu.setVisible(true);
    }

    public void checkBall() { //check if the ball is past the bottom/top of the screen for scoring, and reset if it is
        Body body = ball.getBody();
        int direction;
        //checking the ball and updating scores is handled differently if it's survival mode
        if (mode == Mode.SURVIVAL) {
            if (body.getPosition().y < -PowerPong.NATIVE_HEIGHT / 2 / PowerPong.PPM) {
                if (botScore > topScore)
                    topScore = botScore;
                direction = -1;
                botScore = 0;
            }
            else return;
        } //this is the stuff that happens if it's not survival mode
        else if (body.getPosition().y < -PowerPong.NATIVE_HEIGHT / 2 / PowerPong.PPM) {
            score("top");
            direction = -1;
        }
        else if (body.getPosition().y > PowerPong.NATIVE_HEIGHT / 2 / PowerPong.PPM) {
            score("bot");
            direction = 1;
        }
        else return;
        ball.reset(direction);
        if (p1 instanceof PlayerPaddle)
            ball.pause();
    }

    public void score(String side) {
        if (side.equals("top"))
            topScore += 1;
        else if (side.equals("bot"))
            botScore += 1;
    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.BACK || keyCode == Input.Keys.ESCAPE) {
            if (!ball.isPaused() && mode != Mode.AIBATTLE) {
                ball.pause();
                pausedText.setVisible(true);
            } else {
                dispose();
                game.setScreen(new MenuScreen(game));
            }
            return true;
        }
        return super.keyDown(keyCode);
    }

    public boolean touchDown(int x, int y, int pointer, int button) {
        if (ball.isPaused()) {
            ball.resume();
            pausedText.setVisible(false);
            return false;
        }
        return false;
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
        if (mode != Mode.SURVIVAL)
            p2.dispose();
    }
}
