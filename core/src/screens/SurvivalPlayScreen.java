package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.Body;
import com.powerpong.game.ContactListener;
import com.powerpong.game.PowerPong;
import objects.Ball;
import objects.Wall;
import objects.paddles.PlayerPaddle;

public class SurvivalPlayScreen extends PlayScreen {

    private ContactListener contactListener;

    public SurvivalPlayScreen(PowerPong game) {
        super(game);

        new Wall(0, PowerPong.NATIVE_HEIGHT / PowerPong.PPM / 2, PowerPong.NATIVE_WIDTH, 1, 0, world);

        ball = new Ball("ClassicBall.png", 0, 0, BALL_DIRECTION, BALL_SPEED, world);
        p1 = new PlayerPaddle("ClassicPaddle.png", (-50 + (int)(Math.random() * 101)) / PowerPong.PPM, -1100 / PowerPong.PPM, world, worldCam);
        contactListener = new ContactListener(p1, p2, this);
        world.setContactListener(contactListener);
        //create InputMultiplexer, to handle input on multiple paddles and the ui
        multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(p1);
        multiplexer.addProcessor(stage);
    }

    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //step the physics world the amount of time since the last frame, up to 0.25s
        world.step((float)Math.min(dt, 0.25), 6 ,2);

        checkBall();
        p1.update(dt);
        stage.act(dt);
        topScoreText.setText(Integer.toString(topScore));
        botScoreText.setText(Integer.toString(botScore));
        stage.draw();
        //draw the world
        //current coordinate system is 0,0 is the center of the screen, positive y is up
        game.batch.setProjectionMatrix(worldCam.combined);
        game.batch.begin();
        p1.draw(game.batch);
        ball.draw(game.batch);
        game.batch.end();

        //render fixtures from world; scaled properly because it uses the projection matrix from worldCam, which is scaled properly
        debugRenderer.render(world, worldCam.combined);
    }

    public void checkBall() { //check if the ball is past the bottom/top of the screen for scoring, and reset if it is
        Body body = ball.getBody();
        int direction;
        if (body.getPosition().y < -PowerPong.NATIVE_HEIGHT / 2 / PowerPong.PPM) {
            if (botScore > topScore)
                topScore = botScore;
            direction = -1;
            botScore = 0;
        }
        else return;
        ball.reset(direction);
        pauseBall();
    }

    @Override
    public void dispose() {
        world.dispose();
        p1.dispose();
        debugRenderer.dispose();
        ball.dispose();
        stage.dispose();
    }
}
