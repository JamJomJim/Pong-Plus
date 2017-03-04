package screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pongplus.game.ContactListener;
import com.pongplus.game.Options;
import com.pongplus.game.Options.Mode;
import com.pongplus.game.Options.AI;
import com.pongplus.game.PowerPong;
import objects.*;
import objects.paddles.AIPaddle;
import objects.paddles.Paddle;
import objects.paddles.PlayerPaddle;

public class PlayScreen extends InputAdapter implements Screen {
    static final float GRAVITY = 0f; //-9.8 is -9.8m/s^2, as in real life. I think.
    static final int PADDLE_OFFSET = 1100; //vertical distance from the center of the screen that the paddles are set at

    public Mode mode;
    public Options options;
    public AI ai;

    //ball stuff
    protected float BALL_DIRECTION = (float) Math.PI * 3 / 2;
    protected Ball ball;

    protected Paddle p1, p2;

    private Wall practiceWall;

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


    protected PlayScreen(PowerPong game, Options options) {
        this.game = game;
        this.mode = options.mode;
        this.ai = options.ai;
        this.options = options;

        //GAME WORLD STUFF**********************************************************************************************
        //create physics world and contactlistener
        world = new World(new Vector2(0, GRAVITY), true);
        world.setVelocityThreshold(0.01f);

        worldCam = new OrthographicCamera(PowerPong.NATIVE_WIDTH / PowerPong.PPM,
                PowerPong.NATIVE_HEIGHT / PowerPong.PPM); //scale camera viewport to meters

        topScore = 0;
        botScore = 0;

        //create the side walls (and top wall if it's survival mode)
        new Wall((PowerPong.NATIVE_WIDTH + 2) / PowerPong.PPM / 2, 0, 1, PowerPong.NATIVE_HEIGHT, 0, world); //right wall
        new Wall((-PowerPong.NATIVE_WIDTH - 2) / PowerPong.PPM / 2, 0, 1, PowerPong.NATIVE_HEIGHT, 0, world); //left wall
        if (mode == Mode.SURVIVAL)
            new Wall(0, (PowerPong.NATIVE_HEIGHT + 1) / PowerPong.PPM / 2, PowerPong.NATIVE_WIDTH, 1, 0, world);

        //Creates the initial practice wall.
        if (mode == Mode.PRACTICE) {
            practiceWall = new Wall("ClassicPaddle9.png", 0, PADDLE_OFFSET / PowerPong.PPM, 0, world, options);
            practiceWall.randomizeLocation();
        }
        //create the ball
        ball = new Ball(0, 0, BALL_DIRECTION, world, options);

        //create p1 depending on the mode
        if (mode == Mode.AIBATTLE || mode == Mode.MENUBATTLE)
            p1 = new AIPaddle(0, -PADDLE_OFFSET / PowerPong.PPM, world, ball, options);
        //if Survival mode set a small offset to the X of the player paddle.
        else if (mode == Mode.SURVIVAL) {
            float x = 0;
            while (x == 0)
                x = (float) (Math.random() * 5 - 2.5) / PowerPong.PPM;
            p1 = new PlayerPaddle(x, -PADDLE_OFFSET / PowerPong.PPM, world, worldCam, options);
        } else
            p1 = new PlayerPaddle(0, -PADDLE_OFFSET / PowerPong.PPM, world, worldCam, options);

        //create p2 depending on the mode
        if (mode == Mode.TWOPLAYER)
            p2 = new PlayerPaddle(0, PADDLE_OFFSET / PowerPong.PPM, world, worldCam, options);
        else if (mode == Mode.SURVIVAL || mode == Mode.PRACTICE)
            p2 = null;
        else
            p2 = new AIPaddle(0, PADDLE_OFFSET / PowerPong.PPM, world, ball, options);

        world.setContactListener(new ContactListener(p1, p2, this));

        if (p1 instanceof PlayerPaddle)
            ball.pause(); //ball starts paused
        debugRenderer = new Box2DDebugRenderer(); //displays hitboxes in order to see what bodies "look like"

        //UI STUFF******************************************************************************************************
        stage = new Stage(new FitViewport(PowerPong.NATIVE_WIDTH, PowerPong.NATIVE_HEIGHT), game.batch);
        //create and add the table that fills the entire screen
        //stage.setDebugAll(true);
        Table table = new Table();
        table.setFillParent(true);
        table.right();
        stage.addActor(table);

        //create the table and the labels that will display the score
        score = new Table();
        score.setSkin(game.skin);
        topScoreText = new Label(Integer.toString(topScore), game.skin, "Arial130");
        botScoreText = new Label(Integer.toString(botScore), game.skin, "Arial130");
        score.add(topScoreText).right();
        score.row();
        score.add(botScoreText).right();
        //add it to the stage and position it
        table.add(score);
        score.setX(PowerPong.NATIVE_WIDTH - score.getPrefWidth() / 2);
        score.setY(PowerPong.NATIVE_HEIGHT / 2);
        if (mode == Mode.MENUBATTLE)
            score.setVisible(false);

        //create the menu that's displayed when score limit is reached (in one and two player modes)
        menu = new Table();
        menu.setVisible(false);
        final TextButton buttonRestart = new TextButton("PLAY AGAIN", game.skin);
        buttonRestart.setHeight(160);
        buttonRestart.setWidth(buttonRestart.getPrefWidth() + 50);
        final TextButton buttonMenu = new TextButton("MENU", game.skin);
        buttonRestart.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                restart();
            }
        });
        buttonMenu.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                returnToMenu();
            }
        });

        menu.add(buttonRestart).width(buttonRestart.getWidth()).height(buttonRestart.getHeight());
        menu.row();
        menu.add(buttonMenu).fillX().height(buttonRestart.getHeight());

        stage.addActor(menu);
        menu.setX(PowerPong.NATIVE_WIDTH / 2);
        menu.setY(PowerPong.NATIVE_HEIGHT / 2);

        //create the label that's displayed during pause
        pausedText = new Label("PAUSED", game.skin);
        pausedText.setVisible(false);
        stage.addActor(pausedText);
        pausedText.setX(PowerPong.NATIVE_WIDTH / 2 - pausedText.getPrefWidth() / 2);
        pausedText.setY(PowerPong.NATIVE_HEIGHT / 2 - pausedText.getPrefHeight() / 2);

        //create InputMultiplexer, to handle input on multiple paddles and the ui
        multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(this); //playscreen is first in multiplexer, for handling resuming ball
        multiplexer.addProcessor(stage);
        if (p1 instanceof PlayerPaddle)
            multiplexer.addProcessor(p1);
        if (p2 instanceof PlayerPaddle)
            multiplexer.addProcessor(p2);
    }

    public void render(float dt) {
        if (!pausedText.isVisible() && !menu.isVisible()) { //so that player and ai paddles can't move while the ball is paused;
            //note that it checks if the pausedText is visible, rather than if the ball is paused. This allows paddles to continue moving after the ball resets
            world.step((float) Math.min(dt, 0.25), 6, 2);//step the physics world the amount of time since the last frame, up to 0.25s
            p1.update(dt);
            if (p2 != null)
                p2.update(dt);
            checkBall();
            topScoreText.setText(Integer.toString(topScore));
            botScoreText.setText(Integer.toString(botScore));
        }
        //randomizes the location of the practice wall when needed.
        if (mode == Mode.PRACTICE && practiceWall.needsNewLocation())
            practiceWall.randomizeLocation();
        stage.act(dt);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //draw the world
        //current coordinate system is 0,0 is the center of the screen, positive y is up
        ball.draw(worldCam);
        game.batch.setProjectionMatrix(worldCam.combined); //have to set this every time because when the stage is drawn, it sets it to a different one
        game.batch.begin();
        p1.draw(game.batch);
        if (mode != Mode.SURVIVAL && mode != Mode.PRACTICE)
            p2.draw(game.batch);
        if (mode == Mode.PRACTICE)
            practiceWall.draw(game.batch);
        game.batch.end();
        stage.draw(); //draw the stage (ui elements)

        //render fixtures from world; scaled properly because it uses the projection matrix from worldCam, which is scaled properly
        //debugRenderer.render(world, worldCam.combined);
    }

    public void checkBall() { //check if the ball is past the bottom/top of the screen for scoring, and reset if it is
        if (menu.isVisible())
            return;
        Body body = ball.getBody();
        int direction;
        //checking the ball and updating scores is handled differently if it's survival mode
        if (mode == Mode.SURVIVAL) {
            if (body.getPosition().y < -PowerPong.NATIVE_HEIGHT / 2 / PowerPong.PPM) {
                if (botScore > topScore)
                    topScore = botScore;
                direction = -1;
                botScore = 0;
            } else return;
        } else if (mode == Mode.PRACTICE) {
            if (body.getPosition().y < -PowerPong.NATIVE_HEIGHT / 2 / PowerPong.PPM
                    || body.getPosition().y > PowerPong.NATIVE_HEIGHT / 2 / PowerPong.PPM) {
                if (botScore > topScore)
                    topScore = botScore;
                direction = -1;
                botScore = 0;
                practiceWall.randomizeLocation();
            } else return;
        } else if (body.getPosition().y < -PowerPong.NATIVE_HEIGHT / 2 / PowerPong.PPM) { //this is the stuff that happens if it's not survival mode
            score("top");
            direction = -1;
        } else if (body.getPosition().y > PowerPong.NATIVE_HEIGHT / 2 / PowerPong.PPM) {
            score("bot");
            direction = 1;
        } else return; //return if the ball hasn't passed anywhere that it should be reset
        //check if the score limit has been reached; display the menu and don't reset the ball if it has
        if ((botScore >= options.scoreLimit || topScore >= options.scoreLimit) && (mode == Mode.ONEPLAYER || mode == Mode.TWOPLAYER))
            menu.setVisible(true);
        else
            ball.reset(direction);
        //make aipaddles return to center when ball is reset; they will still offset correctly
        if (p1 instanceof AIPaddle)
            ((AIPaddle) p1).setDestination(0);
        if (p2 instanceof AIPaddle)
            ((AIPaddle) p2).setDestination(0);
        if (p1 instanceof PlayerPaddle)
            ball.pause();
    }

    public void score(String side) {
        if (side.equals("top"))
            topScore += 1;
        else if (side.equals("bot"))
            botScore += 1;
    }

    public void restart() {
        dispose();
        game.setScreen(new PlayScreen(game, options));
    }

    public void returnToMenu() {
        dispose();
        game.setScreen(new MenuScreen(game, options));
    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.BACK || keyCode == Input.Keys.ESCAPE) {
            if (menu.isVisible())
                returnToMenu();
            if (!pausedText.isVisible()) {
                ball.pause();
                pausedText.setVisible(true);
            } else {
                returnToMenu();
            }
            return true;
        }
        return super.keyDown(keyCode);
    }

    public boolean touchDown(int x, int y, int pointer, int button) {
        if (menu.isVisible())
            return false; //return so that the ball isn't resumed if the end of game menu is showing
        if (ball.isPaused()) {
            ball.resume();
            pausedText.setVisible(false);
            return false;
        }
        return false;
    }

    @Override
    public void show() {
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
        if (p2 != null)
            p2.dispose();
    }

    public Mode getMode() {
        return mode;
    }
}
