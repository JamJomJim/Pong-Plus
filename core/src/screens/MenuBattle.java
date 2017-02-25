package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.powerpong.game.ContactListener;
import com.powerpong.game.PowerPong;
import objects.paddles.AIPaddle;
import objects.Ball;

public class MenuBattle extends AIBattle {

    private ContactListener contactListener;

    public MenuBattle(PowerPong game, AI ai) {
        super(game, ai);
    }

    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //step the physics world the amount of time since the last frame, up to 0.25s
        world.step((float)Math.min(dt, 0.25), 6 ,2);

        checkBall();
        p1.update(dt);
        p2.update(dt);
        topScoreText.setText(Integer.toString(topScore));
        botScoreText.setText(Integer.toString(botScore));
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

    //empty pauseBall() method so that when it's called for AIBattle, the ball isn't actually paused
    public void pauseBall() {

    }
}
